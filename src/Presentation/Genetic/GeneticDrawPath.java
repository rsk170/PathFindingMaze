package Presentation.Genetic;

import ApplicationLogic.SearchSpace.Genetic.GeneticMaze;
import ApplicationLogic.SearchSpace.Genetic.GeneticNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GeneticDrawPath extends JPanel {

    private final GeneticMaze geneticMaze;
    private final List<GeneticNode> nodesVisited;

    public GeneticDrawPath(GeneticMaze geneticMaze, List<GeneticNode> nodesVisited) {
        this.geneticMaze = geneticMaze;
        this.nodesVisited = nodesVisited;
    }

    private Color getCellColor(int cellValue) {
        if (cellValue == -1) {
            return Color.GREEN;
        } else if (cellValue == 1) {
            return Color.BLACK;
        } else if (cellValue == 9) {
            return Color.RED;
        }

        return Color.WHITE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = geneticMaze.getSquareSize();

        System.out.println("Genetic " + cellSize + " " + geneticMaze.getColumnNumber() + " " + nodesVisited.size());

        for (int x = 0; x < geneticMaze.getRowNumber(); x++) {    //PAINT EACH NODE IN THE GRID
            for (int y = 0; y < geneticMaze.getColumnNumber(); y++) {
                g.setColor(getCellColor(geneticMaze.getMaze()[x][y]));

                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }

        int index = 0, i, j;
        for (GeneticNode geneticNode : nodesVisited) {
            index = geneticNode.getCellIndex();
            i = index / geneticMaze.getColumnNumber();
            j = index % (geneticMaze.getColumnNumber()) - 1;

            g.setColor(Color.YELLOW);
            g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);

            g.setColor(Color.BLACK);
            g.drawRect(cellSize * j, cellSize * i, cellSize, cellSize);
//            nodeOrder++;
        }
        
        // Start
        drawSingleCell((Graphics2D) g, geneticMaze.getStartIndex(), Color.RED, "");
        // Target
        drawSingleCell((Graphics2D) g, geneticMaze.getTargetIndex(), Color.GREEN, "");
    }


    private void drawSingleCell(Graphics2D g2, int idx, Color nodeColor, String text) {
        if (idx <= 0) return;
        if (geneticMaze.getColumnNumber() <= 0) return;
        if (g2 == null) return;

        int blockSize = geneticMaze.getSquareSize();
        int i = idx / geneticMaze.getColumnNumber();
        int j = (idx % geneticMaze.getColumnNumber()) - 1;

        Rectangle rect = new Rectangle(blockSize * j, blockSize * i, blockSize, blockSize);

        g2.setColor(nodeColor);
        g2.fill(rect);

        g2.setColor(Color.BLACK);
        g2.draw(rect);

        if (text.length() > 0) {
            g2.setColor(Color.BLACK);
            g2.drawString(text,
                    (blockSize * j) + (blockSize / 4),
                    (blockSize * i) + (blockSize / 2));
        }
    }
}