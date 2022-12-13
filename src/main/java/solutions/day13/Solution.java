package solutions.day13;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Solution {

    private enum Result {
        RIGHT_ORDER,
        WRONG_ORDER,
        NO_OUTCOME;
    }

    public int getSolution(List<String> lines) {
        int sumOfIndices = 0;
        List<Integer> indicesInRightOrder = new ArrayList<>();

        int pairIndex = 1;
        int i = 0;
        while (i < lines.size()) {
            String left = lines.get(i);
            String right = lines.get(i + 1);

            if (isInRightOrder(left, right) == Result.RIGHT_ORDER) {
                sumOfIndices += pairIndex;
                indicesInRightOrder.add(pairIndex);
            }

            pairIndex++;
            i += 3;
        }

        System.out.println(indicesInRightOrder);
        return sumOfIndices;
    }

    public Result isInRightOrder(String left, String right) {
        ListElement leftListElement = convertStringToElements(left);
        ListElement rightListElement = convertStringToElements(right);

        return isInRightOrder(leftListElement, rightListElement);

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

    private Result isInRightOrder(ListElement left, ListElement right) {
        int i = 0;
        for (; i < left.elements.size(); i++) {
            if (i >= right.elements.size()) {
                return Result.WRONG_ORDER;
            }
            Element leftValue = left.elements.get(i);
            Element rightValue = right.elements.get(i);
            Result result = isInRightOrder(leftValue, rightValue);
            if (result != Result.NO_OUTCOME) {
                return result;
            }
        }
        if (i < right.elements.size()) { // right still has some items
            return Result.RIGHT_ORDER;
        } else {
            return Result.NO_OUTCOME;
        }
    }

    public Result isInRightOrder(Element left, Element right) {
        if (left instanceof IntElement && right instanceof IntElement) {
            return isInRightOrder(((IntElement) left).value, ((IntElement) right).value);
        } else {
            if (left instanceof IntElement) {
                ListElement listElement = new ListElement();
                listElement.elements.add(left);
                return isInRightOrder(listElement, (ListElement) right);
            } else if (right instanceof IntElement) {
                ListElement listElement = new ListElement();
                listElement.elements.add(right);
                return isInRightOrder((ListElement) left, listElement);
            }
            return isInRightOrder((ListElement) left, (ListElement) right);
        }
    }

    public Result isInRightOrder(int left, int right) {
        if (left < right) {
            return Result.RIGHT_ORDER;
        } else if (left > right) {
            return Result.WRONG_ORDER;
        } else {
            return Result.NO_OUTCOME;
        }
    }

    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day13input.txt"));
        System.out.println(solution.getSolution(lines));
    }
}
