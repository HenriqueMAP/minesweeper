package br.com.otogamidev.minesweeper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the class that represents the game board, which will contain the user selection fields, which
 * will contain bombs or be empty.
 * @since 25-06-2022
 * @implNote Class implementation in lecture 228 of the full Java course.
 * @author henriquematheusalvespereira
 */
public class GameBoard {

    private int boardLines;
    private int boardColumns;
    private int boardUndermines;

    private final List<SelectField> boardFields = new ArrayList<SelectField>();

    /**
     * Constructor method that is responsible for generating the game board selection fields, mapping neighboring
     * fields and minefields to a selected field.
     * @param boardLines Total number of lines on the board.
     * @param boardColumns Total number of columns on the board.
     * @param boardUndermines Total number of undermines on the board.
     */
    public GameBoard(final int boardLines, final int boardColumns, final int boardUndermines) {
        this.boardLines = boardLines;
        this.boardColumns = boardColumns;
        this.boardUndermines = boardUndermines;

        generateBoardFields();
        mappingBoardNeighborhood();
        raffleBoardUndermines();
    }

    /**
     * @return An integer representing the number of lines on the game board.
     */
    private int getBoardLines() { return boardLines; }

    /**
     * @param boardLines Define an integer representing the number of lines on the game board.
     */
    private void setBoardLines(final int boardLines) { this.boardLines = boardLines; }

    /**
     * @return An integer representing the number of columns on the game board.
     */
    private int getBoardColumns() { return boardColumns; }

    /**
     * @param boardColumns Define an integer representing the number of columns on the game board.
     */
    private void setBoardColumns(final int boardColumns) { this.boardColumns = boardColumns; }

    /**
     * @return Returns an integer representing the number of undermines on the game board.
     */
    private int getBoardUndermines() { return boardUndermines; }

    /**
     * @param boardUndermines Returns an integer representing the number of undermines on the game board.
     */
    private void setBoardUndermines(final int boardUndermines) { this.boardUndermines = boardUndermines; }

    /**
     * @return A list containing the defined fields of the board, sorted using rows and columns.
     */
    private List<SelectField> getBoardFields() { return boardFields; }

    /**
     * Method that generates all selection fields on the game board.
     */
    private void generateBoardFields(){
        for(int indexLine = 0; getBoardLines() > indexLine; indexLine++) {
            for(int indexColumn = 0; getBoardColumns() > indexColumn; indexColumn++){
                boardFields.add(new SelectField(indexLine, indexColumn));
            }
        }
    }

    /**
     * Method that maps all fields neighboring the selection field on the game board.
     */
    private void mappingBoardNeighborhood() {
        for(SelectField boardSelectedField: boardFields){
            for(SelectField boardNeighborField: boardFields) {
                boardSelectedField.checkAndAddNeighbor(boardNeighborField);
            }
        }
    }

    /**
     * Method that draws all fields with mines on the game board.
     */
    private void raffleBoardUndermines() {

    }
}
