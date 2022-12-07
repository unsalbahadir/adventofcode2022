package solutions.day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static final int MAX_SIZE_TO_CHECK = 100000;
    private static final int TOTAL_DISK_SPACE = 70000000;
    private static final int NEEDED_EMPTY_SPACE = 30000000;

    public int findTotalSize(List<String> lines) {
        Directory root = buildFileSystem(lines);

        return calculateTotalSize(root);
    }

    private Directory buildFileSystem(List<String> lines) {
        Directory root = new Directory("/", null);
        Directory currentDirectory = root;
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i);
            if (line.startsWith("$")) { // command
                String[] arguments = line.split(" ");
                String command = arguments[1];
                if (command.equals("cd")) {
                    String directoryToGo = arguments[2];
                    if (directoryToGo.equals("/")) {
                        currentDirectory = root;
                    } else if (directoryToGo.equals("..")) {
                        currentDirectory = currentDirectory.parent;
                    } else {
                        currentDirectory = (Directory) currentDirectory.findNode(directoryToGo);
                    }
                } else { // ls
                    // will continue with next iteration
                }
            } else { // ls continuation
                String[] listResult = line.split(" ");
                String dirOrSize = listResult[0];
                String name = listResult[1];
                if (dirOrSize.equals("dir")) {
                    currentDirectory.addNode(new Directory(name, currentDirectory));
                } else {
                    currentDirectory.addNode(new File(name, Integer.parseInt(dirOrSize)));
                }
            }
            i++;
        }

        return root;
    }

    private int calculateTotalSize(Directory directory) {
        int totalSize = 0;

        for (Node child : directory.children) {
            if (child instanceof Directory) {
                Directory childDirectory = (Directory) child;
                int size = childDirectory.getSize();
                if (size < MAX_SIZE_TO_CHECK) {
                    totalSize += size;
                }
                int totalSizeOfChild = calculateTotalSize(childDirectory);
                totalSize += totalSizeOfChild;
            }
        }

        return totalSize;
    }

    public int findSmallestToDelete(List<String> lines) {
        Directory root = buildFileSystem(lines);

        int currentSize = root.getSize();
        int currentEmpty = TOTAL_DISK_SPACE - currentSize;
        int needToDelete = NEEDED_EMPTY_SPACE - currentEmpty;

        List<Directory> directoryToDelete = getDirectoryToDelete(root, needToDelete);

        int minSize = Integer.MAX_VALUE;
        for (Directory directory : directoryToDelete) {
            minSize = Math.min(minSize, directory.getSize());
        }
        return minSize;
    }

    private List<Directory> getDirectoryToDelete(Directory directory, int needToDelete) {
        List<Directory> directoriesPossible = new ArrayList<>();

        for (Node child : directory.children) {
            if (child instanceof Directory) {
                Directory childDirectory = (Directory) child;
                int size = childDirectory.getSize();
                if (size >= needToDelete) {
                    directoriesPossible.add(childDirectory);
                }
                directoriesPossible.addAll(getDirectoryToDelete(childDirectory, needToDelete));
            }
        }

        return directoriesPossible;
    }



    public static void main(String[] args) throws IOException {
        Solution solution = new Solution();

        List<String> lines = Files.readAllLines(Paths.get("inputs/day7input.txt"));
        System.out.println(solution.findTotalSize(lines));
        System.out.println(solution.findSmallestToDelete(lines));
    }
}
