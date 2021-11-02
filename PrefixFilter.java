import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;

public class PrefixFilter {
	public static void main(String[] args) {
		String fileName = null;
		if (args.length >= 1) {
			fileName = args[0];
		} else {
			fileName = "prefixes.yaml";
		}
		ArrayList<FilterTree> filterTrees = null;
		filterTrees = buildFilterTree(readConfig(fileName));
		for (FilterTree tree : filterTrees)
			tree.print();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			boolean found = false;
			String str = scanner.nextLine().trim();

			if (str.isEmpty())
				continue;

			String[] value = str.split("[.]");
			for (FilterTree tree : filterTrees) {
				if (tree.getRoot().getValue().equals(value[0])) {
					found = tree.search(value);
					break;
				}
			}
			System.out.println(str + ":" + found);
		}
		scanner.close();
	}

	private static List<String> readConfig(String fileName) {
		File config = new File(fileName);
		if (config.exists()) {
			try {
				InputStream inputStream = new FileInputStream(config);
				Yaml yaml = new Yaml(new Constructor(SNMPTrap.class));
				Object prefixed = yaml.load(inputStream);

				// System.out.println(prefixed.toString());
				SNMPTrap snmpTrap = (SNMPTrap) prefixed;
				List<String> prefixes = snmpTrap.getPrefixes();
				return prefixes;
			} catch (FileNotFoundException e) {
				System.out.printf("File Not Found: %s\n", new Object[] { fileName });
				printUsage();
				System.exit(0);
			} catch (Exception e) {
				// System.out.println(e);
				e.printStackTrace();
				System.out.println("Provide a valid Yaml file");
				System.exit(0);
			}
		} else {
			System.out.printf("File Not Found: %s\n", new Object[] { fileName });
			printUsage();
			System.exit(0);
		}
		return null;
	}

	private static ArrayList<FilterTree> buildFilterTree(List<String> prefixes) {
		if (null == prefixes || prefixes.size() <= 0) {
			System.out.println("Prefix List is empty");
			printUsage();
			System.exit(0);
		}
		ArrayList<FilterTree> filterTrees = new ArrayList<>();
		for (String prefix : prefixes) {
			String[] values = prefix.split("[.]");
			FilterTree tree = null;
			for (FilterTree t : filterTrees) {
				if (t.getRoot().getValue().equals(values[0])) {
					tree = t;
					break;
				}
			}
			if (tree == null) {
				tree = new FilterTree();
				filterTrees.add(tree);
			}
			tree.insert(values);
		}
		return filterTrees;
	}

	private static void printUsage() {
		System.out.println("usage");
	}

	private static class FilterTree {
		private Node root = null;

		public Node getRoot() {
			return this.root;
		}

		public void insert(String[] value) {
			if (this.root == null)
				this.root = new Node(value[0]);
			insertRecursive(this.root, value, 1);
		}

		public boolean search(String[] value) {
			return searchRecursive(this.root, value, 1);
		}

		public void print() {
			if (this.root != null)
				printRecursive(this.root);
		}

		private void insertRecursive(Node root, String[] value, int index) {
			if (null != root) {
				if (index >= value.length)
					return;
				ArrayList<Node> children = root.getChildren();
				Node newNode = new Node(value[index]);
				for (Node child : children) {
					if (child.getValue().equals(value[index])) {
						insertRecursive(child, value, index + 1);
						return;
					}
				}
				children.add(newNode);
				root.setChildren(children);
				insertRecursive(newNode, value, index + 1);
			}
		}

		private void printRecursive(Node root) {
			if (null != root)
				for (Node child : root.getChildren())
					printRecursive(child);
		}

		private boolean searchRecursive(Node root, String[] value, int index) {
			if (index >= value.length)
				return true;
			ArrayList<Node> children = root.getChildren();
			if (null == children || children.isEmpty())
				return true;
			for (Node child : children) {
				if (child.getValue().equals(value[index]))
					return searchRecursive(child, value, index + 1);
			}
			return false;
		}
	}

	private static class Node {
		private String value;

		private ArrayList<Node> children;

		public Node(String value) {
			this.value = value;
			this.children = new ArrayList<>();
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public ArrayList<Node> getChildren() {
			return this.children;
		}

		public void setChildren(ArrayList<Node> children) {
			this.children = children;
		}

		public String toString() {
			return "Node [value=" + this.value + ", children=" + this.children + "]";
		}
	}

	public static class SNMPTrap {
		private List<String> prefixes;

		public String toString() {
			return "SNMPTrap [prefixes=" + this.prefixes + "]";
		}

		public List<String> getPrefixes() {
			return this.prefixes;
		}

		public void setPrefixes(List<String> prefixes) {
			this.prefixes = prefixes;
		}
	}

}
