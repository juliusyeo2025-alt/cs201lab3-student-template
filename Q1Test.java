import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Q1Test {
     public static void main(String[] args) {

          String result, traversal1, traversal2, traversal3;

          traversal1 = "1-2";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "2-1-3";
          traversal3 = "1-3-2";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "1-2-3";
          traversal2 = "3-2-1";
          traversal3 = "2-3-1";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println("Expected Result : Invalid traversals");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "3-1-2-5-4";
          traversal2 = "2-1-4-5-3";
          traversal3 = "1-2-3-4-5";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();

          traversal1 = "10-20-30-40-50";
          traversal2 = "20-10-30-40-50";
          traversal3 = "10-50-40-30-20";
          result = verify(traversal1, traversal2, traversal3);
          System.out.println("Traversal 1 : " + traversal1);
          System.out.println("Traversal 2 : " + traversal2);
          System.out.println("Traversal 3 : " + traversal3);
          System.out.println(
                    "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
          System.out.println("  Actual Result : " + result);
          System.out.println();
     }

     /**
      * Checks whether the three strings are a valid (Inorder, Preorder, Postorder)
      * combination of one BST, and reports which string is which.
      */
     public static String verify(String traversal1, String traversal2, String traversal3) {
          String[] trav = { traversal1, traversal2, traversal3 };
          int[][] vals = { parse(traversal1), parse(traversal2), parse(traversal3) };

          // Try every assignment of roles to the three inputs.
          int[][] rolePerms = {
                    { 0, 1, 2 }, // in, pre, post indices into trav[]
                    { 0, 2, 1 },
                    { 1, 0, 2 },
                    { 1, 2, 0 },
                    { 2, 0, 1 },
                    { 2, 1, 0 }
          };

          String found = null;
          for (int[] perm : rolePerms) {
               int inIdx = perm[0];
               int preIdx = perm[1];
               int postIdx = perm[2];

               if (!sameMultiset(vals[inIdx], vals[preIdx]) || !sameMultiset(vals[inIdx], vals[postIdx])) {
                    continue;
               }
               if (!isSorted(vals[inIdx])) {
                    continue;
               }

               TreeNode root = buildBstFromPreorder(vals[preIdx]);
               if (root == null && vals[preIdx].length > 0) {
                    continue;
               }

               String in = join(inorder(root));
               String pre = join(preorder(root));
               String post = join(postorder(root));

               if (in.equals(trav[inIdx]) && pre.equals(trav[preIdx]) && post.equals(trav[postIdx])) {
                    String[] labels = new String[3];
                    labels[inIdx] = "Inorder";
                    labels[preIdx] = "Preorder";
                    labels[postIdx] = "Postorder";
                    String candidate = "Traversal 1 - " + labels[0]
                              + ", Traversal 2 - " + labels[1]
                              + ", Traversal 3 - " + labels[2];
                    if (found != null && !found.equals(candidate)) {
                         // Ambiguous labeling — treat as invalid for safety.
                         return "Invalid traversals";
                    }
                    found = candidate;
               }
          }

          return found == null ? "Invalid traversals" : found;
     }

     private static int[] parse(String traversal) {
          if (traversal == null || traversal.isEmpty()) {
               return new int[0];
          }
          String[] parts = traversal.split("-");
          int[] out = new int[parts.length];
          for (int i = 0; i < parts.length; i++) {
               out[i] = Integer.parseInt(parts[i].trim());
          }
          return out;
     }

     private static boolean isSorted(int[] a) {
          for (int i = 1; i < a.length; i++) {
               if (a[i] <= a[i - 1]) {
                    return false;
               }
          }
          return true;
     }

     private static boolean sameMultiset(int[] a, int[] b) {
          if (a.length != b.length) {
               return false;
          }
          int[] x = Arrays.copyOf(a, a.length);
          int[] y = Arrays.copyOf(b, b.length);
          Arrays.sort(x);
          Arrays.sort(y);
          return Arrays.equals(x, y);
     }

     private static String join(List<Integer> list) {
          if (list.isEmpty()) {
               return "";
          }
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < list.size(); i++) {
               if (i > 0) {
                    sb.append('-');
               }
               sb.append(list.get(i));
          }
          return sb.toString();
     }

     /** Build the unique BST whose preorder traversal is the given sequence. */
     private static TreeNode buildBstFromPreorder(int[] pre) {
          TreeNode root = null;
          for (int v : pre) {
               root = insert(root, v);
          }
          return root;
     }

     private static TreeNode insert(TreeNode root, int v) {
          if (root == null) {
               return new TreeNode(v);
          }
          if (v < root.val) {
               root.left = insert(root.left, v);
          } else if (v > root.val) {
               root.right = insert(root.right, v);
          }
          // equal values are not expected per lab statement
          return root;
     }

     private static List<Integer> inorder(TreeNode root) {
          List<Integer> list = new ArrayList<>();
          inorderHelper(root, list);
          return list;
     }

     private static void inorderHelper(TreeNode root, List<Integer> list) {
          if (root == null) {
               return;
          }
          inorderHelper(root.left, list);
          list.add(root.val);
          inorderHelper(root.right, list);
     }

     private static List<Integer> preorder(TreeNode root) {
          List<Integer> list = new ArrayList<>();
          preorderHelper(root, list);
          return list;
     }

     private static void preorderHelper(TreeNode root, List<Integer> list) {
          if (root == null) {
               return;
          }
          list.add(root.val);
          preorderHelper(root.left, list);
          preorderHelper(root.right, list);
     }

     private static List<Integer> postorder(TreeNode root) {
          List<Integer> list = new ArrayList<>();
          postorderHelper(root, list);
          return list;
     }

     private static void postorderHelper(TreeNode root, List<Integer> list) {
          if (root == null) {
               return;
          }
          postorderHelper(root.left, list);
          postorderHelper(root.right, list);
          list.add(root.val);
     }

     static class TreeNode {
          int val;
          TreeNode left;
          TreeNode right;

          TreeNode(int val) {
               this.val = val;
          }
     }
}
