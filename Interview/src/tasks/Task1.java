package tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Task1 {

	public static void main(String[] args) {
		try (BufferedReader brOrders = new BufferedReader(new FileReader("data\\orders.csv"));
				BufferedReader brProducts = new BufferedReader(new FileReader("data\\products.csv"));
				BufferedWriter bw = new BufferedWriter(new FileWriter("order_prices.csv"))) {

			Map<String, Double> products = readProductsAndPrices(brProducts);

			bw.write("id,euros\n"); // Add header

			calculateAndWriteOrderPrices(brOrders, bw, products);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Double> readProductsAndPrices(BufferedReader br) throws IOException {
		Map<String, Double> products = new HashMap<>();
		br.readLine(); // Skip header
		String line;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(",");
			products.put(parts[0], Double.parseDouble(parts[2]));
		}
		return products;
	}

	private static void calculateAndWriteOrderPrices(BufferedReader br, BufferedWriter bw, Map<String, Double> products)
			throws IOException {
		br.readLine(); // Skip header
		String line;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split(",");
			String orderId = parts[0];
			String[] productIds = parts[2].split(" ");
			double totalPrice = calculateOrderTotal(productIds, products);
			bw.write(orderId + "," + totalPrice + "\n");
		}
	}

	private static double calculateOrderTotal(String[] productIds, Map<String, Double> products) {
		double total = 0.0;
		for (String productId : productIds) {
			if (products.containsKey(productId)) {
				total += products.get(productId);
			}
		}
		return total;
	}
}
