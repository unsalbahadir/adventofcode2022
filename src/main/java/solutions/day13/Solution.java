package solutions.day13;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solution {

    public int getSolution(List<String> lines) {
        int sumOfIndices = 0;
        List<Integer> indicesInRightOrder = new ArrayList<>();

        int pairIndex = 1;
        int i = 0;
        while (i < lines.size()) {
            String left = lines.get(i);
            String right = lines.get(i + 1);

            if (isInRightOrder(left, right)) {
                sumOfIndices += pairIndex;
                indicesInRightOrder.add(pairIndex);
            }

            pairIndex++;
            i += 3;
        }

        System.out.println(indicesInRightOrder);
        return sumOfIndices;
    }

    public boolean isInRightOrder(String left, String right) {
        ListElement leftListElement = convertStringToElements(left);
        ListElement rightListElement = convertStringToElements(right);

        return compare(leftListElement, rightListElement) == -1;
    }

    private ListElement convertStringToElements(String line) {
        ListElement result = new ListElement();

        Stack<ListElement> stack = new Stack<>();
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '[') {
                ListElement newListElement = new ListElement();
                stack.add(newListElement);
            } else if (c == ']') {
                // add last number
                if (StringUtils.isNumeric(currentNumber)) {
                    ListElement peek = stack.peek();
                    IntElement intElement = new IntElement(Integer.parseInt(currentNumber.toString()));
                    peek.elements.add(intElement);
                    currentNumber = new StringBuilder();
                }

                // add list to elements
                ListElement pop = stack.pop();
                if (stack.isEmpty()) {
                    result = pop;
                } else {
                    stack.peek().elements.add(pop);
                }
            } else if (c == ',') {
                if (StringUtils.isNumeric(currentNumber)) {
                    ListElement peek = stack.peek();
                    IntElement intElement = new IntElement(Integer.parseInt(currentNumber.toString()));
                    peek.elements.add(intElement);
                    currentNumber = new StringBuilder();
                }
            } else {
                currentNumber.append(c);
            }
        }

        return result;
    }

    private int compare(ListElement left, ListElement right) {
        int i = 0;
        for (; i < left.elements.size(); i++) {
            if (i >= right.elements.size()) {
                return 1;
            }
            Element leftValue = left.elements.get(i);
            Element rightValue = right.elements.get(i);
            int result = compare(leftValue, rightValue);
            if (result != 0) {
                return result;
            }
        }
        if (i < right.elements.size()) { // right still has some items
            return -1;
        } else {
            return 0;
        }
    }

    public int compare(Element left, Element right) {
        if (left instanceof IntElement && right instanceof IntElement) {
            return compare(((IntElement) left).value, ((IntElement) right).value);
        } else {
            if (left instanceof IntElement) {
                ListElement listElement = new ListElement();
                listElement.elements.add(left);
                return compare(listElement, (ListElement) right);
            } else if (right instanceof IntElement) {
                ListElement listElement = new ListElement();
                listElement.elements.add(right);
                return compare((ListElement) left, listElement);
            }
            return compare((ListElement) left, (ListElement) right);
        }
    }

    public int compare(int left, int right) {
        return Integer.compare(left, right);
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day13input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
