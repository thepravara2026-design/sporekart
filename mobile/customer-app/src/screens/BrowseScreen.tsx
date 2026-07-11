import React, { useEffect, useState } from 'react';
import { View, FlatList, TouchableOpacity, Text, StyleSheet, Image } from 'react-native';

interface Product {
  id: string;
  name: string;
  price: number;
  image: string;
  rating: number;
}

/**
 * Customer App Product Browse Screen
 * Displays available products with offline support
 */
export const CustomerBrowseScreen: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    setIsLoading(true);
    try {
      // TODO: Load from API or offline cache
      const mockProducts: Product[] = [
        { id: '1', name: 'Organic Seeds - Pack A', price: 299, image: '', rating: 4.5 },
        { id: '2', name: 'Fertilizer Mix', price: 599, image: '', rating: 4.2 },
        { id: '3', name: 'Garden Tools Set', price: 1299, image: '', rating: 4.8 },
      ];
      setProducts(mockProducts);
    } catch (error) {
      console.error('Failed to load products:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const renderProduct = ({ item }: { item: Product }) => (
    <TouchableOpacity style={styles.productCard}>
      <View style={styles.imageContainer}>
        <Text style={styles.placeholder}>Image</Text>
      </View>
      <Text style={styles.productName}>{item.name}</Text>
      <Text style={styles.price}>₹{item.price}</Text>
      <Text style={styles.rating}>⭐ {item.rating}</Text>
      <TouchableOpacity style={styles.addButton}>
        <Text style={styles.addButtonText}>Add to Cart</Text>
      </TouchableOpacity>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Browse Products</Text>
      {isLoading ? (
        <Text style={styles.loading}>Loading products...</Text>
      ) : (
        <FlatList
          data={products}
          renderItem={renderProduct}
          keyExtractor={(item) => item.id}
          numColumns={2}
          columnWrapperStyle={styles.columnWrapper}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    paddingHorizontal: 10,
  },
  header: {
    fontSize: 24,
    fontWeight: 'bold',
    marginVertical: 16,
    color: '#333',
  },
  columnWrapper: {
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  productCard: {
    flex: 1,
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 12,
    marginHorizontal: 5,
    marginBottom: 10,
  },
  imageContainer: {
    height: 120,
    backgroundColor: '#e0e0e0',
    borderRadius: 6,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 8,
  },
  placeholder: {
    color: '#999',
  },
  productName: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 4,
  },
  price: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#2ecc71',
    marginBottom: 4,
  },
  rating: {
    fontSize: 12,
    marginBottom: 8,
  },
  addButton: {
    backgroundColor: '#2ecc71',
    paddingVertical: 8,
    borderRadius: 6,
    alignItems: 'center',
  },
  addButtonText: {
    color: '#fff',
    fontWeight: '600',
    fontSize: 12,
  },
  loading: {
    textAlign: 'center',
    marginTop: 20,
    color: '#999',
  },
});
