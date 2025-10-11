public class World {
    private final Tile[][] tiles;

    public World(int rows, int cols) {
        tiles = new Tile[rows][cols];
    }

    public void setTile(int row, int col, Tile tile) {
        tiles[row][col] = tile;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public void takeTurn() {
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                Tile t = tiles[r][c];
                if (t != null) t.takeTurn();
            }
        }
    }
}