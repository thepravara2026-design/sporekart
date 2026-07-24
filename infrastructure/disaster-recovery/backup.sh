#!/bin/bash
set -euo pipefail

# SporeKart Enterprise Disaster Recovery Platform
# Automated backup, restore, and recovery procedures

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BACKUP_ROOT="/mnt/backups/sporekart"
TIMESTAMP=$(date +%Y%m%d-%H%M%S)
BACKUP_DIR="${BACKUP_ROOT}/${TIMESTAMP}"
RETENTION_DAYS=30

log() {
  echo "[$(date +%Y-%m-%dT%H:%M:%S%z)] $*"
}

error_exit() {
  log "ERROR: $*"
  exit 1
}

# Ensure backup directory exists
mkdir -p "${BACKUP_DIR}"

case "${1:-help}" in
  backup)
    log "=== SporeKart Enterprise Backup ==="
    log "Backup directory: ${BACKUP_DIR}"

    if [ -n "${DATABASE_URL:-}" ]; then
      log "--- Backing up PostgreSQL database ---"
      pg_dump "${DATABASE_URL}" \
        --format=custom \
        --compress=9 \
        --file="${BACKUP_DIR}/sporekart-database.dump" \
        --verbose \
        --no-owner \
        --no-acl
      log "✅ Database backup complete: $(du -h "${BACKUP_DIR}/sporekart-database.dump" | cut -f1)"
    else
      log "⚠️  DATABASE_URL not set, skipping database backup"
    fi

    if [ -f ".env.production" ]; then
      log "--- Backing up environment configuration ---"
      cp ".env.production" "${BACKUP_DIR}/env.production.backup"
      gpg --symmetric --cipher-algo AES256 \
        --output "${BACKUP_DIR}/env.production.gpg" \
        "${BACKUP_DIR}/env.production.backup"
      rm "${BACKUP_DIR}/env.production.backup"
      log "✅ Env backup encrypted: env.production.gpg"
    fi

    log "--- Backing up Terraform state ---"
    if [ -d "infrastructure/terraform" ]; then
      tar czf "${BACKUP_DIR}/terraform-state.tar.gz" infrastructure/terraform/
      log "✅ Terraform state backed up"
    fi

    log "--- Backing up Kubernetes manifests ---"
    if [ -d "infrastructure/kubernetes" ]; then
      tar czf "${BACKUP_DIR}/kubernetes-manifests.tar.gz" infrastructure/kubernetes/
      log "✅ K8s manifests backed up"
    fi

    log "--- Backing up Docker configuration ---"
    if [ -d "docker" ]; then
      tar czf "${BACKUP_DIR}/docker-config.tar.gz" docker/
      log "✅ Docker config backed up"
    fi

    # Backup metadata
    cat > "${BACKUP_DIR}/backup-metadata.json" <<EOF
{
  "timestamp": "$(date -u +%Y-%m-%dT%H:%M:%SZ)",
  "version": "2.0.0",
  "git_commit": "$(git rev-parse HEAD 2>/dev/null || echo 'unknown')",
  "git_branch": "$(git rev-parse --abbrev-ref HEAD 2>/dev/null || echo 'unknown')",
  "backup_type": "full",
  "contents": ["database", "env", "terraform", "kubernetes", "docker"]
}
EOF
    log "✅ Backup metadata written"

    log "--- Backup Summary ---"
    du -sh "${BACKUP_DIR}"/*

    log "=== Backup complete ==="
    ;;

  restore)
    RESTORE_SOURCE="${2:-latest}"
    log "=== SporeKart Enterprise Restore ==="

    if [ "$RESTORE_SOURCE" = "latest" ]; then
      RESTORE_SOURCE=$(ls -dt "${BACKUP_ROOT}"/*/ 2>/dev/null | head -1)
      if [ -z "$RESTORE_SOURCE" ]; then
        error_exit "No backups found in ${BACKUP_ROOT}"
      fi
      RESTORE_SOURCE="${RESTORE_SOURCE%/}"
    fi

    log "Restoring from: ${RESTORE_SOURCE}"

    if [ -f "${RESTORE_SOURCE}/sporekart-database.dump" ]; then
      log "--- Restoring database ---"
      read -r -p "This will OVERWRITE the current database. Continue? [y/N] " CONFIRM
      if [ "$CONFIRM" = "y" ] || [ "$CONFIRM" = "Y" ]; then
        pg_restore "${RESTORE_SOURCE}/sporekart-database.dump" \
          --clean \
          --if-exists \
          --no-owner \
          --no-acl \
          --verbose \
          --dbname="${DATABASE_URL}"
        log "✅ Database restored"
      else
        log "Skipping database restore"
      fi
    fi

    log "=== Restore complete ==="
    ;;

  list)
    log "=== Available Backups ==="
    for dir in "${BACKUP_ROOT}"/*/; do
      if [ -f "${dir}backup-metadata.json" ]; then
        echo "---"
        cat "${dir}backup-metadata.json"
        echo ""
        du -sh "${dir}"
      fi
    done
    ;;

  cleanup)
    log "=== Cleaning up backups older than ${RETENTION_DAYS} days ==="
    find "${BACKUP_ROOT}" -maxdepth 1 -type d -mtime "+${RETENTION_DAYS}" -exec rm -rf {} \;
    log "Cleanup complete"
    ;;

  verify)
    RESTORE_SOURCE="${2:-latest}"
    if [ "$RESTORE_SOURCE" = "latest" ]; then
      RESTORE_SOURCE=$(ls -dt "${BACKUP_ROOT}"/*/ 2>/dev/null | head -1)
    fi
    log "Verifying backup integrity: ${RESTORE_SOURCE}"
    if [ -f "${RESTORE_SOURCE}/sporekart-database.dump" ]; then
      pg_restore --list "${RESTORE_SOURCE}/sporekart-database.dump" > /dev/null && \
        log "✅ Database dump integrity verified" || \
        log "❌ Database dump corrupted"
    fi
    ;;

  *)
    echo "Usage: $0 {backup|restore|list|cleanup|verify}"
    echo ""
    echo "  backup   - Create full platform backup"
    echo "  restore  - Restore from latest or specified backup"
    echo "  list     - List available backups"
    echo "  cleanup  - Remove backups older than ${RETENTION_DAYS} days"
    echo "  verify   - Verify backup integrity"
    exit 1
    ;;
esac
