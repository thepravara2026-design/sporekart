import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';

interface DealerDashboardData {
  totalOrders: number;
  pendingOrders: number;
  inventory: number;
  monthlyRevenue: number;
}

/**
 * Dealer App Dashboard Screen
 */
export const DealerDashboardScreen: React.FC = () => {
  const [dashboard, setDashboard] = useState<DealerDashboardData>({
    totalOrders: 0,
    pendingOrders: 0,
    inventory: 0,
    monthlyRevenue: 0,
  });

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    // TODO: Load from API
    setDashboard({
      totalOrders: 156,
      pendingOrders: 8,
      inventory: 1250,
      monthlyRevenue: 125000,
    });
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>Dealer Dashboard</Text>

      <View style={styles.statsGrid}>
        <View style={styles.statCard}>
          <Text style={styles.statValue}>{dashboard.totalOrders}</Text>
          <Text style={styles.statLabel}>Total Orders</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statValue}>{dashboard.pendingOrders}</Text>
          <Text style={styles.statLabel}>Pending</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statValue}>{dashboard.inventory}</Text>
          <Text style={styles.statLabel}>Inventory</Text>
        </View>
        <View style={styles.statCard}>
          <Text style={styles.statValue}>₹{dashboard.monthlyRevenue / 1000}K</Text>
          <Text style={styles.statLabel}>Month Revenue</Text>
        </View>
      </View>

      <TouchableOpacity style={styles.actionButton}>
        <Text style={styles.actionButtonText}>New Order</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.actionButton}>
        <Text style={styles.actionButtonText}>Request Quotation</Text>
      </TouchableOpacity>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    paddingHorizontal: 16,
  },
  header: {
    fontSize: 24,
    fontWeight: 'bold',
    marginVertical: 16,
    color: '#333',
  },
  statsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  statCard: {
    width: '48%',
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 16,
    marginBottom: 12,
    alignItems: 'center',
  },
  statValue: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#2ecc71',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 12,
    color: '#666',
  },
  actionButton: {
    backgroundColor: '#3498db',
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: 'center',
    marginBottom: 12,
  },
  actionButtonText: {
    color: '#fff',
    fontWeight: '600',
    fontSize: 16,
  },
});
