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
        int numOfSquares = board.getBoardSize() * board.getBoardSize();
        // stores a list of move lists so we can redo our first moves and
        // keep track of the moves we still need to try
        ArrayList<ArrayList<Integer[]>> testMoveLists = new ArrayList<>();
        testMoveLists.add(getWarnsdorfRuleMoves(board, x, y));
        ArrayList<Integer[]> validMoves = new ArrayList<>();
        int nextSquare = num;
        int currentx = x;
        int currenty = y;
        while (nextSquare <= numOfSquares) {
            validMoves = getWarnsdorfRuleMoves(board, currentx, currenty);
            // backtrack when a dead end is reached
            if (validMoves.isEmpty()) {
                // clear the board
                for (int i = 0; i < board.getBoardSize(); i++) {
                    for (int j = 0; j < board.getBoardSize(); j++)
                        board.setPositionValue(i, j, -1);
                }
                // remove the move that didn't work if there are more than 1 left to test
                if (testMoveLists.getLast().size() > 1) {
                    testMoveLists.getLast().removeFirst();
                }
                // redo the starting moves
                nextSquare = num;
                currentx = x;
                currenty = y;
                board.setPositionValue(currentx, currenty, nextSquare - 1);
                for (ArrayList<Integer[]> testMoveList : testMoveLists) {
                    currentx += testMoveList.getFirst()[0];
                    currenty += testMoveList.getFirst()[1];
                    board.setPositionValue(currentx, currenty, nextSquare);
                    nextSquare++;
                }
                // add another move if there are no more moves to try at the current backtracked space
                if (testMoveLists.getLast().size() == 1) {
                    testMoveLists.add(getWarnsdorfRuleMoves(board, currentx, currenty));
                    currentx += testMoveLists.getLast().getFirst()[0];
                    currenty += testMoveLists.getLast().getFirst()[1];
                    board.setPositionValue(currentx, currenty, nextSquare);
                    nextSquare++;
                }
            } else {
                // make moves untill we hit a dead end or finish
                currentx += validMoves.getFirst()[0];
                currenty += validMoves.getFirst()[1];
                board.setPositionValue(currentx, currenty, nextSquare);
                nextSquare++;
            }
        }
        return true;
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

