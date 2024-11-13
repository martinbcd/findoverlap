import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindOverlappingArea {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean[][] occupiedGrid;
        //the specific name of the employee, their coordinates and the space they requested correlate by their position in the list
        while(sc.hasNextLine()) {
            int width;
            int height;
            int contested = 0;
            List<String> employeeList = new ArrayList<>();
            List<Integer[]> employeeCoordinateList = new ArrayList<>();
            List<Integer> employeeSpaceList = new ArrayList<>();
            String[] sizeString = sc.nextLine().split(" ");
            //the width and height of the area given
            width = Integer.parseInt(sizeString[0]);
            height = Integer.parseInt(sizeString[1]);
            occupiedGrid = new boolean[width][height];
            //total square size of the area calculated here
            int total = width * height;
            //total area unoccupied, subtracted later
            int unallocated = total;
            //n = number of employees
            int n = Integer.parseInt(sc.nextLine());
            //adds employees coordinates and space they requested to their corresponding lists, space is subtracted later
            for (int i = 0; i < n; i++) {
                String[] employeeString = sc.nextLine().split(" ");
                employeeList.add(employeeString[0]);
                int x1 = Integer.parseInt(employeeString[1]);
                int y1 = Integer.parseInt(employeeString[2]);
                int x2 = Integer.parseInt(employeeString[3]);
                int y2 = Integer.parseInt(employeeString[4]);
                Integer[] cord = {x1,y1,x2,y2};
                employeeCoordinateList.add(cord);
                employeeSpaceList.add((x2-x1)*(y2-y1));
            }
            //edge case if there is only one employee it's unnecessary to run the contested algorithms
            if(employeeList.size() == 1){
                unallocated -= employeeSpaceList.get(0);
            }
            //checking if the list is large enough to compare two elements
            //if there are no employees there is no need to calculate contested space
            else if(employeeList.size() > 1) {
                //compares each employee to every other employee,
                //each time space is found to be overlapping the calculateOverlap function tracks it in the occupiedGrid
                for (int i = 0; i < employeeList.size(); i++) {
                    for (int j = i + 1; j < employeeList.size(); j++) {
                        Integer[] personACord = employeeCoordinateList.get(i);
                        Integer[] personBCord = employeeCoordinateList.get(j);
                        //calls function to check if the two individuals share any space
                        //if any is space both individuals requested it is tracked in the occupiedGrid matrix
                        occupiedGrid = calculateOverlap(personACord[0], personACord[1], personACord[2], personACord[3],
                                personBCord[0], personBCord[1], personBCord[2], personBCord[3], occupiedGrid);
                    }
                }
                //goes through each employee to check the finalized occupied grid in order to subtract from their guaranteed and the unallocated space
                for(int i = 0; i < employeeList.size(); i++){
                    Integer[] personCord = employeeCoordinateList.get(i);
                    int employeeSpace = employeeSpaceList.get(i);
                    int newSpace = employeeSpace - countOccupied(personCord[0], personCord[1], personCord[2], personCord[3], occupiedGrid);
                    employeeSpaceList.set(i,newSpace);
                    unallocated -= employeeSpaceList.get(i);
                }
                //counts the total amount of occupied spaces and assigns this value to the contested tracker and removes it from the unallocated space
                int occupied = countOccupied(0, 0, width, height, occupiedGrid);
                contested = occupied;
                unallocated -= occupied;
            }
            System.out.println("Total " +total);
            System.out.println("Unallocated " +unallocated);
            System.out.println("Contested " + contested);
            for(int i = 0; i < employeeList.size(); i++){
                System.out.println(employeeList.get(i) + " " + employeeSpaceList.get(i));
            }
            System.out.println(" ");
        }
        sc.close();

    }
    //given two rectangles in a 2d coordinate system the function finds if they overlap,
    //if they overlap a boolean matrix with the overlapping area marked will be returned
    //if they do not overlap the boolean matrix entered into the function is simply returned
    //explanation of the math involved https://www.geeksforgeeks.org/total-area-two-overlapping-rectangles/
    private static boolean[][] calculateOverlap(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2, boolean[][] grid){
        int ox1 = Math.max(ax1,bx1);
        int oy1 = Math.max(ay1,by1);
        int ox2 = Math.min(ax2,bx2);
        int oy2 = Math.min(ay2,by2);
        int xOverlap = Math.min(ax2,bx2) - Math.max(ax1,bx1);
        int yOverlap = Math.min(ay2,by2) - Math.max(ay1,by1);
        if (xOverlap > 0 && yOverlap > 0){
            return markGrid(ox1,oy1,ox2,oy2,grid);
        }
        return grid;
    }
    //given a grid and a rectangle in a 2d coordinate system it marks each space the rectangle occupies in the boolean matrix
    private static boolean[][] markGrid(int x1, int y1, int x2, int y2, boolean[][] grid){
        for(int i = x1; i < x2; i++){
            for(int j = y1; j < y2; j++){
                grid[i][j] = true;
            }
        }
        return grid;
    }
    //the function counts each cell which is true in the specified rectangular area of the grid
    private static int countOccupied(int x1, int y1, int x2, int y2, boolean[][] grid){
        int total = 0;
        for(int i = x1; i < x2; i++){
            for(int j = y1; j < y2; j++){
                if(grid[i][j]){
                    total++;
                }
            }
        }
        return total;
    }
}
