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
        ArrayList<Integer[]> initialValidMoves = getWarnsdorfRuleMoves(board, x, y);
        ArrayList<Integer[]> validMoves = new ArrayList<>(initialValidMoves);
        int nextSquare = num;
        int currentx = x;
        int currenty = y;
        while (!validMoves.isEmpty()) {
            currentx += validMoves.getFirst()[0];
            currenty += validMoves.getFirst()[1];
            board.setPositionValue(currentx, currenty, nextSquare);
            nextSquare++;
            validMoves = getWarnsdorfRuleMoves(board, currentx, currenty);
        }
        if (nextSquare > numOfSquares) {
            return true;
        }
        // backtrack the board
        for (int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                if (board.getPositionValue(i, j) >= num)
                    board.setPositionValue(i, j, -1);
            }
        }
        // try all other moves from this position
        for (Integer[] move : initialValidMoves) {
            board.setPositionValue(x+move[0], y+move[1], num);
            if (solveKnightsTour(board, x+move[0], y+move[1], num + 1)) {
                return true;
            }
            board.setPositionValue(x+move[0], y+move[1], -1);
        }
        return false;
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

