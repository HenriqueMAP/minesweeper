package br.com.otogamidev.minesweeper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the structure of each square selection field in the game, in which the user must take an
 * action by clicking on it, either to open it or mark it to keep it closed.
 * @since 25-06-2022
 * @implNote Class implementation in lecture 228 of the full Java course.
 * @author henriquematheusalvespereira
 */
public class BoardField {

    private final int fieldLine;
    private final int fieldColumn;

    private boolean fieldOpen = false;
    private boolean fieldUndermine = false;
    private boolean fieldMarked = false;

    private List<BoardField> fieldNeighbors = new ArrayList<>();
    private List<BoardFieldObserver> boardFieldObservers = new ArrayList<>();

    /**
     * Construction method of a game board field.
     * @param fieldLine Line of board field position.
     * @param fieldColumn Column of board field position.
     */
    BoardField(final int fieldLine, final int fieldColumn) {
        this.fieldLine = fieldLine;
        this.fieldColumn = fieldColumn;
    }

    public List<BoardField> getFieldNeighbors() {
        return fieldNeighbors;
    }

    public List<BoardFieldObserver> getBoardFieldObservers() {
        return boardFieldObservers;
    }

    public void registerObserver(final BoardFieldObserver boardFieldObserver){
        getBoardFieldObservers().add(boardFieldObserver);
    }

    private void notifyObservers(final BoardFieldEvents boardFieldEvent){
        getBoardFieldObservers().stream()
                .forEach(boardFieldObserver -> boardFieldObserver.eventOccurred(this, boardFieldEvent));
    };

    /**
     * Check if the field, representing the informed neighbor, is really a neighbor.
     * @param neighbor Field informed as neighbor to be checked.
     * @return If yes, it returns true and adds the informed neighbor, and if not, it returns false.
     * @implNote Lesson 231 - Adding neighbors.
     */
    boolean checkAndAddNeighbor(final BoardField neighbor) {
        boolean differentLine = (getFieldLine() != neighbor.getFieldLine());
        boolean differentColumn = (getFieldColumn() != neighbor.getFieldColumn());
        boolean diagonalLine = (differentLine && differentColumn);

        int deltaLine = Math.abs(getFieldLine() - neighbor.getFieldLine());
        int deltaColumn = Math.abs(getFieldColumn() - neighbor.getFieldColumn());
        int deltaTotal = (deltaLine + deltaColumn);

        if((deltaTotal == 1) && (!diagonalLine)){
            getFieldNeighbors().add(neighbor);
            return true;
        } else if ((deltaTotal == 2) && (diagonalLine)) {
            getFieldNeighbors().add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return True if the field is open, and false if not.
     */
    public boolean isFieldOpen() {
        return fieldOpen;
    }

    /**
     * @param fieldOpen Define if the field is open or not.
     */
    void setFieldOpen(final boolean fieldOpen) {
        this.fieldOpen = fieldOpen;
        if(isFieldOpen()){
            notifyObservers(BoardFieldEvents.TO_OPEN);
        }
    }

    /**
     * @return True if the field is closed, and false if not.
     */
    public boolean isFieldClosed() { return !isFieldOpen(); }

    /**
     * @return True if the field is undermined, and false if not.
     */
    public boolean isFieldUndermine() {
        return fieldUndermine;
    }

    /**
     * @param fieldUndermine Define if the field is undermined or not.
     */
    public void setFieldUndermine(final boolean fieldUndermine) {
        this.fieldUndermine = fieldUndermine;
    }

    /**
     * @return True if the field is marked, and false if not.
     */
    public boolean isFieldMarked() {
        return fieldMarked;
    }

    /**
     * @param fieldMarked Define if the field is marked or not.
     */
    public void setFieldMarked(final boolean fieldMarked) {
        this.fieldMarked = fieldMarked;
    }

    /**
     * Change the state of the field, whether the field is marked or not.
     */
    void changeMarkedField() {
        setFieldMarked(!isFieldMarked());
        if(isFieldMarked()){
            notifyObservers(BoardFieldEvents.TO_MARK_ON);
        } else {
            notifyObservers(BoardFieldEvents.TO_MARK_OFF);
        }
    }

    /**
     * @return Integer number representing the row where the field is positioned.
     */
    public int getFieldLine() { return fieldLine; }

    /**
     * @return Integer number representing the column where the field is positioned.
     */
    public int getFieldColumn() { return fieldColumn; }

    /**
     * @return True if the field is open and with no undermines around it.
     */
    boolean openField() {
        if((isFieldOpen() == false) && (isFieldMarked() == false)) {
            if(isFieldUndermine()) {
                notifyObservers(BoardFieldEvents.TO_EXPLODE);
                return true;
            }
            setFieldOpen(true);
            if(safeNeighborhood()){
                getFieldNeighbors().forEach(neighbor -> neighbor.openField());
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return True if the neighborhood is safe.
     */
    boolean safeNeighborhood(){
        return getFieldNeighbors().stream().noneMatch(neighbor -> neighbor.isFieldUndermine());
    }

    /**
     * @return True if the open field is not mined or if the minefield is marked.
     */
    boolean safeFieldObjective(){
        final boolean safeFieldRevealed = (isFieldUndermine() == false) && (isFieldOpen());
        final boolean safeFieldProtected = (isFieldUndermine()) && (isFieldMarked());
        return safeFieldRevealed || safeFieldProtected;
    }

    /**
     * @return Number of undermines in the neighborhood.
     */
    long quantityOfUnderminesNeighborhood() {
        return getFieldNeighbors().stream().filter(neighbor -> neighbor.isFieldUndermine()).count();
    }

    /**
     * Method that resets the field to its initial state, where it is closed, unchecked, and mine-free.
     */
    void resetSelectedField() {
        setFieldOpen(false);
        setFieldMarked(false);
        setFieldUndermine(false);
    }

    /**
     * @return Each predicted state of the field represented by a character in text format.
     */
    public String toString() {
        final String FIELD_MARKED = "x";
        final String FIELD_EXPLOSION = "*";
        final String FIELD_EMPTY = " ";
        final String FIELD_UNDEFINED = "?";

        if(isFieldMarked()){
            return FIELD_MARKED;
        } else if(isFieldOpen() && isFieldUndermine()){
            return FIELD_EXPLOSION;
        } else if(isFieldOpen() && (quantityOfUnderminesNeighborhood() > 0)){
            return Long.toString(quantityOfUnderminesNeighborhood());
        } else if(isFieldOpen()){
            return FIELD_EMPTY;
        } else {
            return FIELD_UNDEFINED;
        }
    }
}
