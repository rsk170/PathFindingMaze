package Presentation.AStar;

import ApplicationLogic.Algorithms.AStar.AStarAlgorithmData;
import ApplicationLogic.SearchSpace.AStar.AStarMaze;
import ApplicationLogic.SearchSpace.AStar.AStarNode;
import Presentation.Common.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AStarDrawPath extends JPanel implements Runnable {

    private final List<AStarNode> nodesVisited;
    private final List<AStarNode> optimalPath;
    private final AStarNode[][] heuristicMatrix;
    private final AStarAlgorithmData aStarData;
    private final List<AStarNode> drawAnimateVisitedAStarNodes = Collections.synchronizedList(new ArrayList<>());
    private final List<AStarNode> drawAnimatedOptimalPathAStarNodes = Collections.synchronizedList(new ArrayList<>());
    private final int labelStringHeight = 40;

    MainFrame mainFrame = null;
    ReentrantLock lock = new ReentrantLock();

    private final AStarMaze maze;
    private int drawVisitedIndex;
    private int drawOptimalIndex;

    public AStarDrawPath(AStarAlgorithmData aStarData, MainFrame parent) {
        mainFrame = parent;

        this.maze = aStarData.getMaze();
        this.aStarData = aStarData;
        this.nodesVisited = aStarData.getNodesVisited();
        this.optimalPath = aStarData.getOptimalPath();
        this.heuristicMatrix = maze.getNodeArray();

        drawVisitedIndex = 0;
        drawOptimalIndex = 0;

        Thread animator = new Thread(this);
        animator.start();
    }

    private Color getCellColor(int cellValue) {
        if (cellValue == -1) {
            return Color.RED;
        } else if (cellValue == 1) {
            return Color.BLACK;
        } else if (cellValue == 9) {
            return Color.GREEN;
        }

        return Color.WHITE;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                maze.getColumnNumber() * maze.getSquareSize(),
                (maze.getRowNumber() * maze.getSquareSize()) + labelStringHeight);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        drawMazeBoard(g2);

        drawVisitedPath(g2);

        drawOptimalPath(g2);

        // Start Cell Color
        drawSingleCell(g2, maze.getStartIndex(), Color.RED, " 0 ");

        // End Cell Color
        drawSingleCell(g2, maze.getTargetIndex(), Color.GREEN, "");

        if (optimalPath.size() > 0 && drawOptimalIndex == optimalPath.size()) {
            mainFrame.clearShowInfo();
            mainFrame.setShowInfo("Found target path at: " + aStarData.getFinishedAtMSec() + " milliseconds" +
                    System.lineSeparator() +
                    nodesVisited.size() + " nodes visited with " + (optimalPath.size() - 2) + " nodes of optimal path");
        } else {
            if (optimalPath.size() <= 0) {
                mainFrame.clearShowInfo();
                mainFrame.setShowInfo("No target path. Completed at: " + aStarData.getFinishedAtMSec() + " milliseconds");
            }
        }
    }

    private void drawMazeBoard(Graphics2D g2) {
        if (g2 == null) {
            System.out.println("Graphics empty!!!");
            return;
        }
        if (maze == null) return;

        int mazeRowNo = maze.getRowNumber();
        int mazeColNo = maze.getColumnNumber();
        int blockSize = maze.getSquareSize();

        for (int i = 0; i < mazeRowNo; i++) {
            for (int j = 0; j < mazeColNo; j++) {
                Rectangle rect = new Rectangle(j * blockSize, i * blockSize, blockSize, blockSize);
                Color color = getCellColor(maze.getMaze()[i][j]);

                g2.setColor(color);
                g2.fill(rect);

                g2.setColor(Color.BLACK);
                g2.draw(rect);
            }
        }
    }

    private void drawVisitedPath(Graphics2D g2) {
        int idx, i, j;
        int mazeColNo = maze.getColumnNumber();
        if (mazeColNo <= 0) return;

        synchronized (drawAnimateVisitedAStarNodes) {
            for (AStarNode n : drawAnimateVisitedAStarNodes) {
                if (n == null) return;
                idx = n.getCellIndex();
                i = idx / mazeColNo;
                j = (idx % mazeColNo) - 1;

                int heuristicDivider = getHeuristicDivider(mazeColNo);
                Color color = new Color((int) this.heuristicMatrix[i][j].getHeuristicValue() * heuristicDivider % 255,
                        20,
                        100).brighter();
                drawSingleCell(g2, idx, color, "");
//            nodeOrder++;
            }
        }
    }

    private int getHeuristicDivider(int mazeColNo) {
        int heuristicDivider;
        if (mazeColNo <= 15) {
            heuristicDivider = 9;
        } else if (mazeColNo <= 20) {
            heuristicDivider = 8;
        } else if (mazeColNo <= 25) {
            heuristicDivider = 7;
        } else if (mazeColNo <= 30) {
            heuristicDivider = 6;
        } else if (mazeColNo <= 35) {
            heuristicDivider = 4;
        } else {
            heuristicDivider = 6;
        }
        return heuristicDivider;
    }

    private void drawOptimalPath(Graphics2D g2) {
        int idx;
        int nodeOrder = optimalPath.size();

        synchronized (drawAnimatedOptimalPathAStarNodes) {
            for (AStarNode AStarNode : drawAnimatedOptimalPathAStarNodes) {
                if (AStarNode == null) return;
                idx = AStarNode.getCellIndex();
                drawSingleCell(g2, idx, Color.YELLOW, "" + nodeOrder);
                nodeOrder--;
            }
        }
    }

    private void drawSingleCell(Graphics2D g2, int idx, Color nodeColor, String text) {
        if (idx <= 0) return;
        if (maze.getColumnNumber() <= 0) return;
        if (g2 == null) return;

        int blockSize = maze.getSquareSize();
        int i = idx / maze.getColumnNumber();
        int j = (idx % maze.getColumnNumber()) - 1;

        Rectangle rect = new Rectangle(blockSize * j, blockSize * i, blockSize, blockSize);

        g2.setColor(nodeColor);
        g2.fill(rect);

        g2.setColor(Color.BLACK);
        g2.draw(rect);

        //the string on the blocks which show the count of nodes 
        /*if (text.length() > 0) {
            g2.setColor(Color.BLACK);
            g2.drawString(text,
                    (blockSize * j) + (blockSize / 4),
                    (blockSize * i) + (blockSize / 2));
        }*/
    }

    @Override
    public void run() {
        while (true) {
            if (drawVisitedIndex == nodesVisited.size()) {
                if (drawOptimalIndex == optimalPath.size()) {
                    break;
                }

                lock.lock();
                drawAnimatedOptimalPathAStarNodes.add(optimalPath.get(drawOptimalIndex));
                lock.unlock();
                repaint();
                drawOptimalIndex++;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            lock.lock();
            while (true) {
                if (drawVisitedIndex >= nodesVisited.size())
                    continue;
                AStarNode AStarNode = nodesVisited.get(drawVisitedIndex);
                drawAnimateVisitedAStarNodes.add(AStarNode);

                drawVisitedIndex++;
                boolean found = false;
                for (AStarNode oAStarNode : optimalPath) {
                    if (oAStarNode.getCellIndex() == AStarNode.getCellIndex()) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            lock.unlock();

            repaint();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
