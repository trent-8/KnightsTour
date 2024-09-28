import java.util.ArrayList;
/**
 *
 * @author nbreems
 */
public final class KnightsTourSolver {
    /**
     *
     * @param board
     * @param x
     * @param y
     * @param num The number of the next square to go to
     * @return
     */
    static public boolean solveKnightsTour(KnightsTourBoard board, int x, int y, int num) {
        ArrayList<ArrayList<Integer[]>> listOfMoveLists = new ArrayList<>();
        ArrayList<Integer[]> validMoves = getWarnsdorfRuleMoves(board, x, y);
        for (Integer[] move : validMoves) {
            ArrayList<Integer[]> moveList = new ArrayList<>();
            moveList.add(move);
            listOfMoveLists.add(moveList);
        }
        return solveKnightsTourRecursive(board, x, y, num, listOfMoveLists);
    }

    /**
     * attempts to solve using warnsdorf's rule from a given point
     * 
     * @param board
     * @param x
     * @param y
     * @param num The number of the next square to go to
     * @return 
     */
    static public boolean solveKnightsTourRecursive(KnightsTourBoard board, int x, int y, int num, ArrayList<ArrayList<Integer[]>> listOfMoveLists) {
        ArrayList<ArrayList<Integer[]>> newListOfMoveLists = new ArrayList<>();
        for (ArrayList<Integer[]> moveList : listOfMoveLists) {
            // clear the board
            for (int i = 0; i < board.getBoardSize(); i++) {
                for (int j = 0; j < board.getBoardSize(); j++) {
                    if (board.getPositionValue(i, j) > 1)
                        board.setPositionValue(i, j, -1);
                }
            }
            // apply move list
            int currentx = x;
            int currenty = y;
            int nextnum = 2;
            for (Integer[] move : moveList) {
                currentx += move[0];
                currenty += move[1];
                board.setPositionValue(x, y, nextnum);
                nextnum++;
            }
            // find branching move lists and try to solve them
            for (Integer[] move : getWarnsdorfRuleMoves(board, x, y)) {
                ArrayList<Integer[]> newMoveList = new ArrayList<>(moveList);
                newMoveList.add(move);
                newListOfMoveLists.add(newMoveList);
                Integer[] testMove = newListOfMoveLists.getLast().getLast();
                if (attemptToSolve(board, currentx + testMove[0], currenty + testMove[1], nextnum)) {
                    return true;
                }
            }
        }
        return solveKnightsTourRecursive(board, x, y, num, newListOfMoveLists);
    }

    /**
     *
     * @param board
     * @param x
     * @param y
     * @param num The number of the next square to go to
     * @return false if solving fails, true if solving succeeds
     */
    static public boolean attemptToSolve(KnightsTourBoard board, int x, int y, int num) {
        int numOfSquares = board.getBoardSize() * board.getBoardSize();
        ArrayList<Integer[]> validMoves = new ArrayList<>(getWarnsdorfRuleMoves(board, x, y));
        int nextSquare = num;
        int currentx = x;
        int currenty = y;
        // backtrack the board
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getPositionValue(i, j) >= num)
                    board.setPositionValue(i, j, -1);
            }
        }
        while (!validMoves.isEmpty()) {
            currentx += validMoves.getFirst()[0];
            currenty += validMoves.getFirst()[1];
            board.setPositionValue(currentx, currenty, nextSquare);
            nextSquare++;
            validMoves = getWarnsdorfRuleMoves(board, currentx, currenty);
        }
        return nextSquare > numOfSquares;
    }

    /**
     *
     * @param board
     * @param x the x coordinate of the square to move from
     * @param y the y coordinate of the square to move from
     * @return ArrayList of moves that follow warnsdorf's rule
     */
    static public ArrayList<Integer[]> getWarnsdorfRuleMoves(KnightsTourBoard board, int x, int y) {
        Integer[][] possibleMoves = {{1,2},{1,-2},{-1,-2},{-1,2},{2,1},{2,-1},{-2,-1},{-2,1}};
        int boardSize = board.getBoardSize();
        int lowestNumOfMoves = 8;
        ArrayList<Integer[]> validMoves = new ArrayList<>();
        // loop through all moves from the current space
        for (int i = 0; i < 8; i++) {
            int testx = x + possibleMoves[i][0];
            int testy = y + possibleMoves[i][1];
            // check if move is valid
            if ((testx < boardSize) && (testy < boardSize) && (testx >= 0) && (testy >= 0) && (board.getPositionValue(testx, testy) == -1)) {
                int numOfMoves = 0;
                // find the number of possible moves after each move
                for (int j = 0; j < 8; j++) {
                    int test2x = testx + possibleMoves[j][0];
                    int test2y = testy + possibleMoves[j][1];
                    if ((test2x < boardSize) && (test2y < boardSize) && (test2x >= 0) && (test2y >= 0) && (board.getPositionValue(test2x, test2y) == -1))
                    numOfMoves++;
                }
                if (numOfMoves <= lowestNumOfMoves) {
                    if (numOfMoves < lowestNumOfMoves){
                        lowestNumOfMoves = numOfMoves;
                        validMoves.clear();
                    }
                    validMoves.add(possibleMoves[i]);
                }
            }
        }
        return validMoves;
    }
}
