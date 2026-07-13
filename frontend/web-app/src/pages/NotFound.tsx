import PlaceholderPanel from '../components/ui/PlaceholderPanel';

export default function NotFound() {
  return (
    <div className="sk-content__header">
      <h1>Not found</h1>
      <PlaceholderPanel
        title="Page not found"
        hint="This route is not part of the current navigation blueprint. Use the sidebar or ⌘K to navigate."
      />
    </div>
  );
}
