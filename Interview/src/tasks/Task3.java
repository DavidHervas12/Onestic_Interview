package tasks;

import java.io.*;
import java.util.*;

public class Task3 {

	public static void main(String[] args) {
		String ordersFile = "data\\orders.csv";
		String productsFile = "data\\products.csv";
		String customersFile = "data\\customers.csv";
		String outputFile = "customer_ranking.csv";

		try {
			Map<Integer, Double> productIdToCost = readProducts(productsFile);
			Map<Integer, String[]> customerIdToName = readCustomers(customersFile);
			Map<Integer, Double> customerIdToTotalEuros = calculateTotalEuros(ordersFile, productIdToCost);

			List<Map.Entry<Integer, Double>> sortedCustomers = new ArrayList<>(customerIdToTotalEuros.entrySet());
			Collections.sort(sortedCustomers, (a, b) -> b.getValue().compareTo(a.getValue()));

			writeCustomerRanking(outputFile, sortedCustomers, customerIdToName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<Integer, Double> readProducts(String productsFile) throws IOException {
		Map<Integer, Double> productIdToCost = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(productsFile))) {
			br.readLine(); // Skip header
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				int productId = Integer.parseInt(parts[0]);
				double cost = Double.parseDouble(parts[2]);
				productIdToCost.put(productId, cost);
			}
		}
		return productIdToCost;
	}

	private static Map<Integer, String[]> readCustomers(String customersFile) throws IOException {
		Map<Integer, String[]> customerIdToName = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(customersFile))) {
			br.readLine(); // Skip header
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				int customerId = Integer.parseInt(parts[0]);
				String[] name = { parts[1], parts[2] };
				customerIdToName.put(customerId, name);
			}
		}
		return customerIdToName;
	}

	private static Map<Integer, Double> calculateTotalEuros(String ordersFile, Map<Integer, Double> productIdToCost)
			throws IOException {
		Map<Integer, Double> customerIdToTotalEuros = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(ordersFile))) {
			br.readLine(); // Skip header
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				int customerId = Integer.parseInt(parts[1]);
				String[] productIds = parts[2].split(" ");
				double totalEuros = 0.0;
				for (String productId : productIds) {
					int productIdInt = Integer.parseInt(productId);
					totalEuros += productIdToCost.getOrDefault(productIdInt, 0.0);
				}
				customerIdToTotalEuros.put(customerId,
						customerIdToTotalEuros.getOrDefault(customerId, 0.0) + totalEuros);
			}
		}
		return customerIdToTotalEuros;
	}

	private static void writeCustomerRanking(String outputFile, List<Map.Entry<Integer, Double>> sortedCustomers,
			Map<Integer, String[]> customerIdToName) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
			bw.write("id,firstname,lastname,total_euros\n"); // Add header
			for (Map.Entry<Integer, Double> entry : sortedCustomers) {
				int customerId = entry.getKey();
				double totalEuros = entry.getValue();
				String[] name = customerIdToName.get(customerId);
				bw.write(customerId + "," + name[0] + "," + name[1] + "," + totalEuros + "\n");
			}
		}
	}
}
