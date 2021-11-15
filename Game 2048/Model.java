package com.javarush.task.task35.task3513;

import java.util.*;


public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score = 0; // текущий счёт плитки
    int maxTile = 2; // максимальный вес плитки на игровом поле
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    Tile[][] getGameTiles() {
        return gameTiles;
    }

    /**
     * Создаем поле игры
     */
    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    /**
     * Возвращаем список пустых плиток
     */
    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (Tile[] tiles : gameTiles) {
            for (Tile t : tiles) {
                if (t.isEmpty()) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    /**
     * addTile изменяет значение случайной пустой плитки в массиве gameTiles
     * на 2 или 4 с вероятностью 0.9 и 0.1 соответственно
     */
    private void addTile() {
        List<Tile> list = getEmptyTiles();
        if (!list.isEmpty()) {
            int randomTile = (int) (Math.random() * list.size() % list.size());
            Tile t = list.get(randomTile);
            t.value = (Math.random() < 0.9 ? 2 : 4);
        }
    }

    /**
     * Метод для сжатия плиток, чтобы все пустые плитки были справа
     */
    private boolean compressTiles(Tile[] tiles) {
        int insertPosition = 0;
        boolean result = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (!tiles[i].isEmpty()) {
                if (i != insertPosition) {
                    tiles[insertPosition] = tiles[i];
                    tiles[i] = new Tile();
                    result = true;
                }
                insertPosition++;
            }
        }
        return result;
    }

        /**
         * метод для слияния плиток одного номинала
         */
        private boolean mergeTiles(Tile[] tiles){
            boolean result = false;
            LinkedList<Tile> list = new LinkedList<>();
            for (int i = 0; i < FIELD_WIDTH; i++) {
                if (tiles[i].isEmpty()) {
                    continue;
                }
                if (i < FIELD_WIDTH - 1 && tiles[i].value == tiles[i + 1].value) {
                    int tempTile = tiles[i].value * 2;
                    if (tempTile > maxTile) {
                        maxTile = tempTile;
                    }
                    score += tempTile;
                    list.addLast(new Tile(tempTile));
                    tiles[i + 1].value = 0;
                    result = true;
                } else {
                    list.addLast(new Tile(tiles[i].value));
                }
                tiles[i].value = 0;
            }
            for (int i = 0; i < list.size(); i++) {
                tiles[i] = list.get(i);
            }
            return result;
        }

    /**
     * left() - перемещаем плитки влево
     */

    public void left(){
        if(isSaveNeeded){
            saveState(gameTiles);
        }
            boolean flag = false;
            for (int i = 0; i < FIELD_WIDTH; i++) {
                if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])){
                    flag = true;
                }
            }
            if (flag) {
                addTile();
            }
            isSaveNeeded = true;
        }

    /**
     * rotationOn90DegreesRight - переворачиваем матрицу на 90гр вправо
     */
    private Tile[][] rotationOn90DegreesRight(Tile[][] gameTiles){
       Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                temp[i][j] = gameTiles[FIELD_WIDTH - j - 1][i];
            }
        }
        return temp;
    }

    /**
     * down() - опускаем плитки вниз
     */
    public void down(){
        saveState(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        left();
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
    }

    /**
     * up() - поднимаем плитки вверх
     */
    public void up(){
        saveState(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        left();
        gameTiles = rotationOn90DegreesRight(gameTiles);
    }

    /**
     * right() - перемещаем плитки вправо
     */
    public void right(){
        saveState(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        left();
        gameTiles = rotationOn90DegreesRight(gameTiles);
        gameTiles = rotationOn90DegreesRight(gameTiles);
        
    }

    private int getEmptyTilesCount() {
        return getEmptyTiles().size();
    }

    private boolean isFull() {
        return getEmptyTilesCount() == 0;
    }

    /**
     * canMove() - возвращает true в случае, если в текущей позиции возможно сделать ход так,
     * чтобы состояние игрового поля изменилось
     */

    boolean canMove() {
        if (!isFull()) {
            return true;
        }

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                Tile t = gameTiles[x][y];
                if ((x < FIELD_WIDTH - 1 && t.value == gameTiles[x + 1][y].value)
                        || ((y < FIELD_WIDTH - 1) && t.value == gameTiles[x][y + 1].value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * saveState() - будет сохранять текущее
     * игровое состояние и счет в стеки
     */
    private void saveState(Tile[][] gameTiles){
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                temp[i][j] = new Tile(gameTiles[i][j].value);
            }
        }
        previousStates.push(temp);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    /**
     * rollback() - будет устанавливать текущее игровое состояние
     * равным последнему находящемуся в стеках
     */
    public void rollback(){
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    /**
     *  randomMove() - делаем рандомный ход
     */
    public void randomMove(){
        int n = ((int) (Math.random() * 100)) % 4;
        switch(n){
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }

    /**
     * hasBoardChanged - будет возвращать true, в случае,
     * если вес плиток в массиве gameTiles отличается от веса плиток
     * в верхнем массиве стека previousStates
     */
    private boolean hasBoardChanged(){
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if(gameTiles[i][j].value != previousStates.peek()[i][j].value){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getMoveEfficiency(Move move) - описывает эффективность переданного хода
     * @param move
     * @return
     */
   private MoveEfficiency getMoveEfficiency(Move move){
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        move.move();
        if (hasBoardChanged()){
            moveEfficiency = new MoveEfficiency(getEmptyTilesCount(), score, move);
        }
        rollback();
        return moveEfficiency;
    }

    /**
     * autoMove() - автоход
     */
    public void autoMove(){
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.offer(getMoveEfficiency(this::left));
        queue.offer(getMoveEfficiency(this:: right));
        queue.offer(getMoveEfficiency(this::up));
        queue.offer(getMoveEfficiency(this::down));

        queue.peek().getMove().move();


    }
}

