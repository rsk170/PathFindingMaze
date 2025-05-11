package Presentation.Common;

import ApplicationLogic.Algorithms.AStar.AStarAlgorithm;
import ApplicationLogic.Algorithms.Genetic.GeneticAlgorithmData;
import ApplicationLogic.SearchSpace.Genetic.GeneticNode;
import ApplicationLogic.Algorithms.Genetic.GeneticAlgorithm;
import ApplicationLogic.Algorithms.Exceptions.PathNotFound;
import AccessMazes.GetMazeArray;
import Presentation.AStar.AStarDrawPath;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class MainFrame extends JFrame {

    private final int viewWidth = 810;
    private final int viewHeight = 750;
    private final int controlPanelWidth = 190;
    private final int mazeDrawSize = 600;
    private final int controlPanelHeight = viewHeight;
    private final int topInfoWidth = mazeDrawSize;
    private final int topInfoHeight = 25;
    private final int bottomInfoWidth = mazeDrawSize;
    private final int bottomInfoHeight = 65;

    private final String[] algorithms = {"A*", "Genetic"};
    private final String[] dimensions = {"15x15", "20x20", "25x25", "30x30", "35x35"};
    private final String[] mazeNo = {"1", "2", "3", "4"};

    DrawMazeAndGenetic mazeView;
    GeneticAlgorithmData geneticAlgorithmData;
    GeneticAlgorithm geneticAlgo;
    AStarDrawPath aStarPath;

    private int cellCount = 15;
    private int cellSize = mazeDrawSize / cellCount;
    private int[][] mazeArray = new int[cellCount][cellCount];
    private DrawAlgorithm drawAlgorithm = DrawAlgorithm.NoAlgorithm;
    private JLabel lblCellsCount;
    private JTextArea lblInfo;
    private JLabel lblTopInfo;
    private JComboBox<String> cmbMazeDimensions;
    private JComboBox<String> cmbMazeNo;
    private JComboBox<String> cmbAlgorithms;

    private final JButton btnStartSearch = new JButton("Start Search");
    private final JButton btnResetMazeView = new JButton("Reset");
    private final JButton btnGenerateMazeView = new JButton("Generate maze");
    private final JButton btnClearMazeView = new JButton("Clear maze");

    public MainFrame() {
        setTitle("Path Finder View");
        setSize(viewWidth, viewHeight);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        createMazeNodes();

        JPanel controlsPanel = new JPanel();

        cmbMazeDimensions = new JComboBox<>(dimensions);
        cmbMazeNo = new JComboBox<>(mazeNo);
        cmbAlgorithms = new JComboBox<>(algorithms);

        lblCellsCount = new JLabel(cellCount + "x" + cellCount);

        JLabel lblAlgorithms = new JLabel("Choose algorithm:");
        JLabel lblMazeDimensions = new JLabel("Choose size:");
        JLabel lblMazeNo = new JLabel("Choose type of maze:");
        JLabel lblMazeSize = new JLabel("Size:");

        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        controlsPanel.setBorder(BorderFactory.createTitledBorder(loweredetched, "Controls"));

        int componentWidth = 160;
        int componentHeight = 30;

        int componentX = 15;
        int componentY = 20;
        int diff = componentHeight + 15;

        controlsPanel.setLayout(null);
        controlsPanel.setBounds(1, 1, controlPanelWidth, controlPanelHeight);

        lblMazeDimensions.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(lblMazeDimensions);
        componentY += diff;

        cmbMazeDimensions.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(cmbMazeDimensions);
        componentY += diff;

        lblMazeNo.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(lblMazeNo);
        componentY += diff;

        cmbMazeNo.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(cmbMazeNo);
        componentY += diff;

        btnGenerateMazeView.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(btnGenerateMazeView);
        componentY += diff;

        btnClearMazeView.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(btnClearMazeView);
        componentY += diff + 40;

        lblAlgorithms.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(lblAlgorithms);
        componentY += diff;

        cmbAlgorithms.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(cmbAlgorithms);
        componentY += diff;

        btnStartSearch.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(btnStartSearch);
        componentY += diff;

        btnResetMazeView.setBounds(componentX, componentY, componentWidth, componentHeight);
        controlsPanel.add(btnResetMazeView);
        componentY += diff + 40;

        lblMazeSize.setBounds(componentX, componentY, componentWidth - 80, componentHeight);
        controlsPanel.add(lblMazeSize);

        lblCellsCount.setBounds(componentX + 80, componentY, componentWidth - 80, componentHeight);
        controlsPanel.add(lblCellsCount);

        add(controlsPanel);

        componentX = controlPanelWidth + 10;
        mazeView = new DrawMazeAndGenetic(cellSize, cellCount, mazeArray);
        mazeView.setBounds(componentX, 1, mazeDrawSize, mazeDrawSize);
        add(mazeView);

        componentY = mazeDrawSize + 5;
        lblTopInfo = new JLabel("");
        lblTopInfo.setBounds(componentX, componentY, topInfoWidth, topInfoHeight);
        add(lblTopInfo);

        lblInfo = new JTextArea("");
        lblInfo.setEditable(false);
        lblInfo.setLineWrap(true);
        lblInfo.setWrapStyleWord(true);

        componentY = mazeDrawSize + topInfoHeight + 10;
        JScrollPane areaScrollPane = new JScrollPane(lblInfo);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setBounds(componentX, componentY, bottomInfoWidth, bottomInfoHeight);

        DefaultCaret caret = (DefaultCaret)lblInfo.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(areaScrollPane);

        btnStartSearch.addActionListener(e -> startPathSearch());
        btnResetMazeView.addActionListener(e -> resetMazeView());
        btnGenerateMazeView.addActionListener(e -> generateMazeView());
        btnClearMazeView.addActionListener(e -> clearMazeView());
        cmbAlgorithms.addItemListener(e -> updateAlgorithms());
        cmbMazeDimensions.addItemListener(e -> updateMazeDimensions());
    }

    private void startPathSearch() {
        lblInfo.setText("");
        lblTopInfo.setText("");

        if (cmbAlgorithms.getSelectedIndex() == 0) {
            drawAlgorithm = DrawAlgorithm.AStarAlgorithm;

            AStarAlgorithm aStarAlgo;
            try {
                aStarAlgo = new AStarAlgorithm(mazeArray, mazeDrawSize);
                aStarAlgo.performSearch();
                aStarPath = new AStarDrawPath(aStarAlgo.getData(), this);
            } catch (Exception | PathNotFound ex) {
                System.out.println(ex.getMessage());
                lblInfo.setText(ex.getMessage());
            }

            if (aStarPath != null) {
                aStarPath.setBounds(controlPanelWidth + 10, 1, mazeDrawSize, mazeDrawSize);
                add(aStarPath);
            }

        } else if (cmbAlgorithms.getSelectedIndex() == 1) {
            drawAlgorithm = DrawAlgorithm.GeneticAlgorithm;

            lblInfo.setText("");

            geneticAlgo = new GeneticAlgorithm(mazeArray, this);
            geneticAlgo.performSearch();
        }
    }

    private void resetMazeView() {
        updateMazeView();
        enableAll(true);
    }

    private void updateMazeView() {
        if (aStarPath != null) {
            getContentPane().remove(aStarPath);

            add(mazeView);
        }

        if (geneticAlgo != null) {
            geneticAlgo.stopSearch();
        }

        geneticAlgorithmData = null;
        cellSize = mazeDrawSize / cellCount;
        lblCellsCount.setText(cellCount + "x" + cellCount);
        mazeView.setCellSize(cellSize);
        mazeView.setCellCount(cellCount);
        mazeView.setMazeArray(mazeArray);
        lblInfo.setText("");
        lblTopInfo.setText("");
    }

    private void generateMazeView() {
        generateMaze();
        updateMazeView();
    }

    private void generateMaze() {
        String selectedMazeNoStr = (String) cmbMazeNo.getSelectedItem();
        assert selectedMazeNoStr != null;
        int selectedMazeNo = Integer.parseInt(selectedMazeNoStr);

        drawAlgorithm = DrawAlgorithm.NoAlgorithm;
        mazeArray = GetMazeArray.Maze(cellCount, selectedMazeNo);
        mazeView.setMazeArray(mazeArray);
    }

    private void createMazeNodes() {
        mazeArray = GetMazeArray.Maze(cellCount, 0);
    }

    private void clearMazeView() {
        createMazeNodes();

        updateMazeView();
    }

    private void updateAlgorithms() {
        updateMazeView();
    }

    private void updateMazeDimensions() {
        String selectedDimension = (String) cmbMazeDimensions.getSelectedItem();
        assert selectedDimension != null;
        cellCount = Integer.parseInt(selectedDimension.split("x")[0]);

        drawAlgorithm = DrawAlgorithm.NoAlgorithm;
        createMazeNodes();
        updateMazeView();
    }

    public void addGeneticDraw(GeneticAlgorithmData geneticAlgorithmData) {
        if (aStarPath != null) {
            remove(aStarPath);
            add(mazeView);
        }
        this.geneticAlgorithmData = geneticAlgorithmData;
        this.drawAlgorithm = DrawAlgorithm.GeneticAlgorithm;
        mazeView.repaint();
    }

    public void setTopInfo(String info) {
        lblTopInfo.setText(info);
    }

    public void setShowInfo(String info) {
        lblInfo.append(info);
    }

    public void clearShowInfo() {
        lblInfo.setText("");
    }

    public void enableAll(boolean enable) {
        btnStartSearch.setEnabled(enable);
        btnClearMazeView.setEnabled(enable);
        btnGenerateMazeView.setEnabled(enable);

        cmbAlgorithms.setEnabled(enable);
        cmbMazeDimensions.setEnabled(enable);
        cmbMazeNo.setEnabled(enable);
    }

    public int getMazeDrawSize() {
        return mazeDrawSize;
    }

    private class DrawMazeAndGenetic extends JPanel {

        private int cellSize;
        private int cellCount;
        private int[][] mazeArray;

        public DrawMazeAndGenetic(int cellSize, int cellCount, int[][] mazeNodes) {
            this.cellSize = cellSize;
            this.cellCount = cellCount;
            this.mazeArray = mazeNodes;
        }

        public void setCellSize(int cellSize) {
            this.cellSize = cellSize;
            repaint();
        }

        public void setCellCount(int cellCount) {
            this.cellCount = cellCount;
            repaint();
        }

        public void setMazeArray(int[][] mazeArray) {
            this.mazeArray = mazeArray;
            repaint();
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

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics2D = (Graphics2D) g;

            if (graphics2D == null) return;

            if (mazeArray.length <= 0 || mazeArray[0].length <= 0)
                return;

            for (int i = 0; i < cellCount; i++) {
                for (int j = 0; j < cellCount; j++) {
                    Rectangle rect = new Rectangle(i * cellSize, j * cellSize, cellSize, cellSize);

                    graphics2D.setColor(getCellColor(mazeArray[j][i]));
                    graphics2D.fill(rect);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.draw(rect);
                }
            }

            if (drawAlgorithm == DrawAlgorithm.GeneticAlgorithm) {
                if (geneticAlgorithmData == null) return;

                int index, i, j;
                for (GeneticNode node : geneticAlgorithmData.getNodesVisited()) {
                    index = node.getCellIndex();
                    i = index / cellCount;
                    j = (index % cellCount) - 1;

                    graphics2D.setColor(Color.YELLOW);
                    graphics2D.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }

                index = geneticAlgorithmData.getGeneticMaze().getStartIndex();
                i = index / cellCount;
                j = (index % cellCount) - 1;
                Rectangle rect = new Rectangle(j * cellSize, i * cellSize, cellSize, cellSize);
                graphics2D.setColor(Color.RED);
                graphics2D.fill(rect);
                graphics2D.setColor(Color.BLACK);
                graphics2D.draw(rect);

                index = geneticAlgorithmData.getGeneticMaze().getTargetIndex();
                i = index / cellCount;
                j = (index % cellCount) - 1;
                rect = new Rectangle(j * cellSize, i * cellSize, cellSize, cellSize);
                graphics2D.setColor(Color.GREEN);
                graphics2D.fill(rect);
                graphics2D.setColor(Color.BLACK);
                graphics2D.draw(rect);
            }
        }
    }
}
