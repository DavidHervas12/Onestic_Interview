package tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task2 {

	public static void main(String[] args) {
		String ordersFile = "data\\orders.csv";
		String outputFile = "product_customers.csv";

		try {
			List<Set<Integer>> productCustomersList = readOrders(ordersFile);

			writeProductCustomers(productCustomersList, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<Set<Integer>> readOrders(String ordersFile) throws IOException {
		List<Set<Integer>> productCustomersList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(ordersFile))) {
			br.readLine(); // Skip header
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				String customerId = parts[1];
				String[] productIds = parts[2].split(" ");
				for (String productId : productIds) {
					int productIdInt = Integer.parseInt(productId);
					while (productCustomersList.size() <= productIdInt) {
						productCustomersList.add(new HashSet<>());
					}
					Set<Integer> customers = productCustomersList.get(productIdInt);
					customers.add(Integer.parseInt(customerId));
					productCustomersList.set(productIdInt, customers);
				}
			}
		}
		return productCustomersList;
	}

	private static void writeProductCustomers(List<Set<Integer>> productCustomersList, String outputFile)
			throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
			bw.write("id,customer_ids\n"); // Add header
			for (int productId = 0; productId < productCustomersList.size(); productId++) {
				Set<Integer> customers = productCustomersList.get(productId);
				StringBuilder customerIdsBuilder = new StringBuilder();
				for (int customerId : customers) {
					customerIdsBuilder.append(customerId).append(" ");
				}
				// Delete the last space
				if (customerIdsBuilder.length() > 0) {
					customerIdsBuilder.deleteCharAt(customerIdsBuilder.length() - 1);
				}
				bw.write(productId + "," + customerIdsBuilder.toString() + "\n");
			}
		}
	}
}