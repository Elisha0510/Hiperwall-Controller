package com.hiperwall;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Controller_main extends JFrame {

	private JTextField ipTextField;
	private JTextField portTextField;
	private JTextField contentsTextField;
	public String ip = "127.0.0.1";
	public String port = "8000";

	private JFrame frame;
	private static JFrame inputFrame;
	@SuppressWarnings("exports")
	public JLayeredPane layeredPane;
	private JTree tree;
	JPanel topPanel;
	JPanel bottomPanel;
	@SuppressWarnings("exports")
	public JScrollPane treeScrollPane;
	private JPanel rightPanel;
	JSplitPane mainSplitPane;
	JSplitPane leftSplitPane;
	JButton manageContentButton;
	public static String baseURL = "";

	private List<openFileList> openFileList = new ArrayList<openFileList>();
	private JPanel[] dropPanels = new JPanel[8];
	GridBagConstraints gbc;

	private static ArrayList<String> controllerFileList = new ArrayList<>();
	private static ArrayList<String> folderNameList = new ArrayList<>();
	private static ArrayList<DraggablePanel> dragPanelOpenList = new ArrayList<>();
	private static ArrayList<DraggablePanel> clickPanel = new ArrayList<>();
	public int panelResult = 0;
	public String rootPath = "C:\\Users\\Public\\Hiperwall\\contents";
//   private String rootPath = "C:\\Users\\Public\\Hiperwall\\contents";

	DefaultMutableTreeNode rootTree;
	DragSource dragSource;

	public Controller_main() {
		// IP, PORT, Contents 경로를 입력할 수 있는 Frame과 Panel 만들기
		inputFrame = new JFrame("IP/PORT/CONTENTS 입력창");
		inputFrame.setSize(500, 300);
		inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inputFrame.setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// "IP : " 라벨 생성 및 위치 지정
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 10, 10);
		centerPanel.add(new JLabel("IP :"), gbc);
		// IP 입력할 수 있는 텍스트 필드 생성
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 10, 10);
		ipTextField = new JTextField();
		ipTextField.setPreferredSize(new Dimension(200, 25));
		centerPanel.add(ipTextField, gbc);
		// "PORT : " 라벨 생성 및 위치 지정
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 10, 10);
		centerPanel.add(new JLabel("PORT :"), gbc);
		// PORT 입력할 수 있는 텍스트 필드 생성
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 10, 10);
		portTextField = new JTextField();
		portTextField.setPreferredSize(new Dimension(200, 25));
		centerPanel.add(portTextField, gbc);
		// "CONTENTS : " 라벨 생성 및 위치 지정
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 10, 10);
		centerPanel.add(new JLabel("CONTENTS :"), gbc);
		// CONTENTS 입력할 수 있는 텍스트 필드 생성
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 10, 10);
		contentsTextField = new JTextField();
		contentsTextField.setPreferredSize(new Dimension(200, 25));
		centerPanel.add(contentsTextField, gbc);
		// 확인 버튼 생성
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 0, 0, 0);
		JButton confirmButton = new JButton("확인");
		// 확인 버튼 이벤트 처리
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ipAddress = ipTextField.getText();
				String portAddress = portTextField.getText();
				String contentsDirectory = contentsTextField.getText();
				// 입력되지 않았을 때 기본값 설정
				if (ipAddress.equals("")) {
					ipAddress = ip;
				}
				if (portAddress.equals("")) {
					portAddress = port;
				}
				if (contentsDirectory.equals("")) {
					contentsDirectory = rootPath;
				}
				// 사용자가 입력한 정보들을 확인할 수 있는 창 만들기
				Boolean result = onConfirmButtonClick(ipAddress, portAddress, contentsDirectory);
				if (result) {
					ip = ipAddress;
					port = portAddress;
					rootPath = contentsDirectory;

					// IP, PORT를 가지고 baseURL 생성
					String url = makeURL();
					new Controller_main(url, rootPath);
					inputFrame.dispose();
				}

			}
		});

		centerPanel.add(confirmButton, gbc);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		inputFrame.add(mainPanel);

		inputFrame.setVisible(true);
	}

	public Controller_main(String url, String root) {
		rootPath = root;
		// 컨트롤러 Frame 생성
		frame = new JFrame("Control System Interface");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1150, 800);

		// CONTENTS 경로 폴더와 파일을 Tree로 변환 후 JTree 객체 생성
		rootTree = getFile(rootPath);
		tree = new JTree(rootTree);
		tree.setRootVisible(false);

		// tree를 드래그 소스로 지정
		DragSource dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_COPY, new FileDragGestureListener());

		// tree에 대한 마우스 이벤트
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = tree.getSelectionPath();
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();

					for (String item : folderNameList) {
						if (item.equals(selectedNode.toString())) {
							break;
						}
					}
				}
			}
		});

		// 지우기 버튼 생성 후 버튼 이벤트 처리
		JButton deleteButton = new JButton("지우기");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 마지막으로 클릭한 객체 가져오기
				int lastIndex = clickPanel.size() - 1;
				DraggablePanel getPanel = clickPanel.get(lastIndex);

				// 오픈된 객체 중 클릭한 객체가 있는지 확인 후 삭제
				int selectedIndex = -1;
				if (openFileList.size() != 0) {
					for (int idx = 0; idx < openFileList.size(); idx++) {
						if (openFileList.get(idx).id.equals(getPanel.getName())) {
							selectedIndex = idx;
							sendDeleteXML(openFileList.get(idx).id);
						}
					}
				}
				// 오픈된 객체 저장하는 배열에서 삭제한 객체 삭제
				openFileList.remove(selectedIndex);

				// 삭제된 객체의 DraggablePanel이 있는 LayeredPane 가져오기
				JLayeredPane layeredPane2 = frame.getLayeredPane();

				// 삭제할 객체를 찾아서 LayeredPane에서 삭제
				for (int idx = 0; idx < dragPanelOpenList.size(); idx++) {
					if (dragPanelOpenList.get(idx).getName().equals(getPanel.getName())) {
						selectedIndex = idx;
						layeredPane2.remove(dragPanelOpenList.get(idx));
						layeredPane2.repaint();
						break;
					}
				}
				// 삭제한 객체 배열에서도 삭제
				dragPanelOpenList.remove(selectedIndex);
			}
		});

		// 전체 지우기 버튼 생성 후 버튼 이벤트 처리
		JButton deleteAllButton = new JButton("전체 지우기");
		deleteAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// 삭제할 객체들의 DraggablePanel이 있는 LayeredPane 가져오기
				JLayeredPane layeredPane2 = frame.getLayeredPane();

				// 오픈되어 있는 모든 객체들을 LayeredPane에서 삭제하기
				for (DraggablePanel panel : dragPanelOpenList) {
					layeredPane2.remove(panel);
					layeredPane2.repaint();
				}

				// 오픈되어 있는 모든 객체들을 하이퍼월 컨트롤러에게 삭제 명령 내리기
				if (openFileList.size() != 0) {
					for (openFileList item : openFileList) {
						sendDeleteXML(item.id);
					}
				}
				layeredPane.repaint();

				// 배열 요소 전체 삭제
				openFileList.clear();
				dragPanelOpenList.clear();
			}
		});

		// 소리 끄기 버튼 생성 후 버튼 이벤트 처리
		JButton muteButton = new JButton("소리 끄기");
		muteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 마지막으로 클릭한 객체를 가지고 온다
				int lastIndex = clickPanel.size() - 1;
				DraggablePanel getPanel = clickPanel.get(lastIndex);

				// 클릭한 객체가 열려있는지 확인 후 동영상일 경우 Mute 명령 보내기
				for (openFileList item : openFileList) {
					if (item.id.equals(getPanel.getName()) && item.name.endsWith(".hwv")) {
						sendVideoMute(item.id);
						break;
					}
				}
			}
		});

		// 전체 소리 끄기 버튼 생성 후 버튼 이벤트 처리
		JButton muteAllButton = new JButton("전체 소리 끄기");
		muteAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 현재 오픈되어 있는 객체 중에 모든 동영상 객체에 Mute 명령 보내기
				if (openFileList.size() != 0) {
					for (openFileList item : openFileList) {
						if (item.name.endsWith(".hwv")) {
							sendVideoMute(item.id);
						}
					}
				}
			}
		});

		// 소리바 생성
		JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		volumeSlider.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		volumeSlider.setMajorTickSpacing(20);
		volumeSlider.setMinorTickSpacing(20);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);

		// 소리바에 대한 이벤트 처리
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// 소리 볼륨값 가져오기
				int volume = volumeSlider.getValue();

				// 오픈된 객체 중 모든 동영상 객체의 볼륨을 조절
				if (openFileList.size() != 0) {
					for (openFileList item : openFileList) {
						if (item.name.endsWith(".hwv")) {
							sendVideoVolume(item.id, volume);
						}
					}
				}
			}

		});

		// 프로그램 종료 버튼 생성과 버튼에 대한 이벤트 처리
		JButton exitButton = new JButton("프로그램 종료");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// 컨트롤러 왼쪽 위 패널 생성
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

		// 지우기, 전체 지우기, 소리 끄기, 전체 소리 끄기, 소리바, 프로그램 종료 버튼 추가
		topPanel.add(deleteButton, BorderLayout.CENTER);
		topPanel.add(deleteAllButton, BorderLayout.CENTER);
		topPanel.add(muteButton, BorderLayout.CENTER);
		topPanel.add(muteAllButton, BorderLayout.CENTER);
		topPanel.add(volumeSlider, BorderLayout.CENTER);
		topPanel.add(exitButton, BorderLayout.CENTER);

		setLocationRelativeTo(null);

		// 왼쪽 아래 패널 생성
		bottomPanel = new JPanel(new BorderLayout());

		// 폴더, 파일 트리를 넣을 수 있는 ScrollPane 생성
		treeScrollPane = new JScrollPane(tree);
		// ScrollPane 왼쪽 아래 패널에 추가
		bottomPanel.add(treeScrollPane, BorderLayout.CENTER);

		// 수직으로 분할
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		leftSplitPane.setDividerLocation(200);

		// 오른쪽 패널을 생성합니다
		rightPanel = createRightPanel();
		// 오른쪽 패널에 Drop 할 수 있도록 설정
		new DropTarget(rightPanel, new PanelDropTargetListener());

		// 수평으로 분할
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, rightPanel);
		mainSplitPane.setDividerLocation(250);

		frame.getContentPane().add(mainSplitPane);
		frame.setVisible(true);
	}

	private Boolean onConfirmButtonClick(String ipAddress, String portAddress, String contentsAddress) {
		// 사용자가 입력한 정보를 확인 할 수 있는 창
		int result = JOptionPane.showConfirmDialog(null, "입력된 IP: " + ipAddress + "\n입력된 PORT: " + portAddress
				+ "\nCONTENTS 경로: " + contentsAddress + "\n\n입력한 정보가 맞으면 확인을 눌러주세요", "확인",
				JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			return true;
		}
		return false;
	}

	private String makeURL() {
		baseURL = "http://" + ip + ":" + port;
		return baseURL;
	}

	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.ipadx = 192; // 컴포넌트의 가로 크기에 20 픽셀을 추가
		gbc.ipady = 108; // 컴포넌트의 세로 크기에 20 픽셀을 추가

		gbc.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < 8; i++) {
			dropPanels[i] = new JPanel();
			dropPanels[i].setBorder(BorderFactory.createLineBorder(Color.black));
			dropPanels[i].setBackground(i % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
			// 각 패널의 위치 설정
			gbc.gridx = i % 4; // 열 위치
			gbc.gridy = i / 4; // 행 위치
			rightPanel.add(dropPanels[i], gbc);
		}

		return rightPanel;
	}

	private int getPanelNumber(int x, int y) {
		if (29 <= x && x <= 230) {
			if (262 <= y && y <= 381) {
				return 1;
			} else if (382 <= y && y <= 500) {
				return 5;
			}
		} else if (230 <= x && x <= 431) {
			if (262 <= y && y <= 381) {
				return 2;
			} else if (382 <= y && y <= 500) {
				return 6;
			}
		} else if (431 <= x && x <= 632) {
			if (262 <= y && y <= 381) {
				return 3;
			} else if (382 <= y && y <= 500) {
				return 7;
			}
		} else if (632 <= x && x <= 833) {
			if (262 <= y && y <= 381) {
				return 4;
			} else if (382 <= y && y <= 500) {
				return 8;
			}
		}
		return 0;
	}

	private class FileDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			StringSelection transferable = null;
			TreePath path = tree.getSelectionPath();
			if (path != null) {
				transferable = new StringSelection(path.getLastPathComponent().toString());
			} else if (path == null) {
				Component draggedComponent = dge.getComponent();

				if (draggedComponent instanceof DraggablePanel) {
					DraggablePanel draggablePanel = (DraggablePanel) draggedComponent;
					String idValue = draggablePanel.getName();
					System.out.println("id : " + idValue);

					transferable = new StringSelection(idValue);
				}
			}
			dge.startDrag(null, transferable, new FileDragSourceListener());
		}

	}

	private class FileDragSourceListener implements DragSourceListener {
		@Override
		public void dragEnter(DragSourceDragEvent dsde) {

		}

		@Override
		public void dragOver(DragSourceDragEvent dsde) {

			Component draggedComponent = dsde.getDragSourceContext().getComponent();
			DraggablePanel selectPanel = null;

			if (draggedComponent instanceof DraggablePanel) {
				DraggablePanel draggablePanel = (DraggablePanel) draggedComponent;
				String idValue = draggablePanel.getName();
				for (DraggablePanel panel : dragPanelOpenList) {
					if (panel.getName().equals(idValue)) {
						selectPanel = panel;
						break;
					}
				}
			}
			if (selectPanel != null) {
				Point locationOnScreen = frame.getLocationOnScreen();
				int x = (int) locationOnScreen.getX();
				int y = (int) locationOnScreen.getY();
				selectPanel.setBounds(dsde.getX() - x - 100, dsde.getY() - y - 100, selectPanel.currentWidth,
						selectPanel.currentHeight);
			}

		}

		@Override
		public void dropActionChanged(DragSourceDragEvent dsde) {

		}

		@Override
		public void dragExit(DragSourceEvent dse) {

		}

		@Override
		public void dragDropEnd(DragSourceDropEvent dsde) {
		}
	}

	private class PanelDropTargetListener extends Container implements DropTargetListener {
		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				Transferable transferable = dtde.getTransferable();
				DataFlavor[] flavors = transferable.getTransferDataFlavors();

				if (flavors.length > 0) {

					Object data = transferable.getTransferData(flavors[0]);
					String name = data.toString();

					DraggablePanel draggablePanel = null; // dropSendSizeChangeXML을 사용하기 위해 필요
					for (DraggablePanel panel : dragPanelOpenList) {
						if (panel.getName().equals(name)) {
							draggablePanel = panel;
							break;
						}
					}

					// 파일이 open이 된 파일인지 확인
					String openedName = "";
					boolean openedResult = false;
					for (openFileList listObject : openFileList) {
						openedResult = listObject.id.equals(name);
						if (openedResult) {
							openedName = listObject.id;
							break;
						}
					}

					Point point = dtde.getLocation();
					int pointX = (int) point.getX();
					int pointY = (int) point.getY();
					panelResult = getPanelNumber(pointX, pointY);

					if (openedResult && draggablePanel != null) {
						draggablePanel.dropSendSizeChangeXML(name, panelResult, draggablePanel.ratioW,
								draggablePanel.ratioH, 0, 0);

					} else if (!openedResult) {
						boolean fileSearchResult = false;
						int fileIndexSearchResult = -1;
						for (int idx = 0; idx < controllerFileList.size(); idx++) {
							fileSearchResult = controllerFileList.get(idx).contains(name);
							if (fileSearchResult) {
								fileIndexSearchResult = idx;
								break;
							}
						}

						if (fileSearchResult) {

							String filePath = controllerFileList.get(fileIndexSearchResult);

							dropSendOpenXML(name, panelResult, filePath);

							((DefaultTreeModel) tree.getModel()).reload();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}

	}

	class HitBoxPanel extends JPanel { // 패널 클래스로 만들어서 추가 조정
		private ArrayList<Rectangle> hitBoxes;

		public HitBoxPanel(ArrayList<Rectangle> hitBoxes) {
			this.hitBoxes = new ArrayList<>(hitBoxes);
			setOpaque(false); // 기존 설정값과 동일하게
		}

		public void setHitBoxes(ArrayList<Rectangle> hitBoxes) {
			this.hitBoxes = hitBoxes;
			repaint(); // 호출되면 새로운 hitBoxes로 다시 그림
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;// .create();
			g2d.setColor(Color.RED);
			for (Rectangle rect : hitBoxes) {
				g2d.fill(rect);
			}
			// g2d.dispose();
		}

		public void clear() {
			// TODO Auto-generated method stub

		}
	}

	class DraggablePanel extends JPanel {
		Point initialClick;
		JLabel label;
		Component sourceComponent;
		boolean pressed_flag;

		private final int cornerSize = 10; // 조절점의 크기
		private ArrayList<Rectangle> hitBoxes = new ArrayList<>();
		private int hitBoxIndex = -1; // 선택된 조절점이 없을 때 -1
		private boolean isSelected = false;
		private HitBoxPanel hitBoxPanel; // 패널 추가해줌
		private int currentWidth, currentHeight;
		private float ratioW, ratioH;
		private float originalWidth, originalHeight;
		private Image originalImage;

		public DraggablePanel(String text) {
			setLayout(new BorderLayout());

//         int desiredWidth = 112;
//         int desiredHeight = 72;
			currentWidth = 204;// desiredWidth;
			currentHeight = 119;// desiredHeight;
			ratioW = 1.0f;
			ratioH = 1.0f;
			originalWidth = currentWidth;
			originalHeight = currentHeight;

			ImageIcon imageIcon = new ImageIcon(text);
			originalImage = imageIcon.getImage();
			Image scaledImage = originalImage.getScaledInstance(currentWidth, currentHeight, Image.SCALE_SMOOTH);// desiredWidth,
			// desiredHeight,
			// Image.SCALE_SMOOTH);
			ImageIcon scaledIcon = new ImageIcon(scaledImage);
			this.label = new JLabel(scaledIcon);

			this.label.setBounds(0, 0, currentWidth, currentHeight);// desiredWidth;

			createHitBoxes();
			hitBoxPanel = new HitBoxPanel(hitBoxes);
			hitBoxPanel.setBounds(0, 0, currentWidth, currentHeight);// desiredWidth;

			JLayeredPane layeredPane = new JLayeredPane();
			layeredPane.setPreferredSize(new Dimension(currentWidth, currentHeight));// desiredWidth;
			layeredPane.add(this.label, Integer.valueOf(0)); // JLabel을 기본 레이어에 추가
			layeredPane.add(hitBoxPanel, Integer.valueOf(1)); // HitBoxPanel을 레이어 1에 추가

			this.add(layeredPane, BorderLayout.CENTER);

			DragSource dragSource = DragSource.getDefaultDragSource();
			dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY,
					new FileDragGestureListener());

			addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					Component c = (Component) e.getSource();
					c.setFocusable(true);
					c.requestFocus();

					if (c instanceof DraggablePanel) {
						DraggablePanel addPanel = (DraggablePanel) c;
						clickPanel.add(addPanel);
					}

					initialClick = e.getPoint();

					for (int i = 0; i < hitBoxes.size(); i++) {
						if (hitBoxes.get(i).contains(initialClick)) {
							hitBoxIndex = i;
							isSelected = true;
							// repaint();
							revalidate();// 컴포넌트 크기, 레이아웃 계산
							return;
						}
					}
					if (!isSelected) {
						isSelected = true;
						createHitBoxes();
						repaint();
					} else if (isSelected) {
						isSelected = false;
						hitBoxes.clear();
						repaint();
					}

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getComponent() instanceof DraggablePanel) {
						DraggablePanel releasedPanel = (DraggablePanel) e.getComponent();
						releasedPanel.setSize(releasedPanel.currentWidth, releasedPanel.currentHeight);
						releasedPanel.label.setSize(releasedPanel.currentWidth, releasedPanel.currentHeight);
						revalidate();
						hitBoxIndex = -1;
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					// 드래그중인 패널을 식별합니다.
					Component component = e.getComponent();
					if (component instanceof DraggablePanel) {
						DraggablePanel draggedPanel = (DraggablePanel) component;

						initialClick = e.getPoint();
						// 초기 클릭과 현재 위치의 차이를 구함
						int deltaX = e.getX() - initialClick.x;
						int deltaY = e.getY() - initialClick.y;

						if (hitBoxIndex != -1 && isSelected) { // 조절점이 선택된 경우
							// System.out.println("hitBox 값" + hitBoxIndex + "셀렉 여부" + isSelected);
							resizePanel(e.getPoint());
						} else if (hitBoxIndex == -1 && isSelected) {
							// 드래그중인 패널의 f새 위치로 이동
							int newX = draggedPanel.getX() + deltaX;
							int newY = draggedPanel.getY() + deltaY;
							draggedPanel.setLocation(newX, newY);
							// draggedPanel.setBounds(newX, newY, draggedPanel.currentWidth,
							// draggedPanel.currentHeight);
							// draggedPanel.label.setSize(draggedPanel.currentWidth,
							// draggedPanel.currentHeight);
						}

						// dragPanelOpenList에서 해당 패널을 찾아 속성을 업데이트합니다.
						for (DraggablePanel panel : dragPanelOpenList) {
							if (panel.getName().equals(draggedPanel.getName())) {
								panel.setSize(draggedPanel.getWidth(), draggedPanel.getHeight());
								break;
							}
						}
						createHitBoxes();
						repaint();
					}
				}
			});

			addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					System.out.println(isSelected);
					if (e.getKeyCode() == KeyEvent.VK_DELETE && isSelected) {
						Component pressedComponent = e.getComponent();
						JLayeredPane layeredPane = frame.getLayeredPane();

						if (pressedComponent instanceof DraggablePanel) {
							DraggablePanel draggablePanel = (DraggablePanel) pressedComponent;
							String idValue = draggablePanel.getName();

							for (DraggablePanel panel : dragPanelOpenList) {
								if (panel.getName().equals(idValue)) {
									sendDeleteXML(idValue);
									// dragPanelOpenList에서 panel 지우기
									layeredPane.remove(pressedComponent);
									layeredPane.repaint();
									dragPanelOpenList.remove(panel);
									pressed_flag = false;
									break;
								}
							}
						}
					}

					// 좌우위아래 조절
					if (e.getKeyCode() == KeyEvent.VK_DOWN && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						if (!clickPanel.isEmpty()) {
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

//                   for (openFileList item : openFileList) {
//                      if (item.id.equals(clickPanel.get(lastIndex).getName())) {
//                         beforePanel = item.panelNumber;
//                         panelID = item.id;
//                      }
//                   }

						if (1 <= beforePanel && beforePanel <= 4) {
							int afterPanel = beforePanel + 4;
							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}

							int uiPointArray[] = getUiPoint(afterPanel);

							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}

						}

					}
					if (e.getKeyCode() == KeyEvent.VK_UP && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						if (!clickPanel.isEmpty()) {
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

						if (5 <= beforePanel && beforePanel <= 8) {
							int afterPanel = beforePanel - 4;

							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}

							int uiPointArray[] = getUiPoint(afterPanel);

							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}
//                      clickPanel.get(lastIndex).setBounds(uiPointArray[0], uiPointArray[1],
//                            clickPanel.get(lastIndex).getPreferredSize().width,
//                            clickPanel.get(lastIndex).getPreferredSize().height);
//                      clickPanel.get(lastIndex).revalidate();

						}
					}
					// 숫자 바꾸기

					if (e.getKeyCode() == KeyEvent.VK_LEFT && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						if (!clickPanel.isEmpty()) {
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

//                   for (openFileList item : openFileList) {
//                      if (item.id.equals(clickPanel.get(lastIndex).getName())) {
//                         beforePanel = item.panelNumber;
//                         panelID = item.id;
//                      }
//                   }

						if (2 <= beforePanel && beforePanel <= 4 || 6 <= beforePanel && beforePanel <= 8) {
							int afterPanel = beforePanel - 1;

							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}

							int uiPointArray[] = getUiPoint(afterPanel);

							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}

//                      clickPanel.get(lastIndex).setBounds(uiPointArray[0], uiPointArray[1],
//                            clickPanel.get(lastIndex).getPreferredSize().width,
//                            clickPanel.get(lastIndex).getPreferredSize().height);
//                      clickPanel.get(lastIndex).revalidate();

						}
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						if (!clickPanel.isEmpty()) {
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

						if (1 <= beforePanel && beforePanel <= 3 || 5 <= beforePanel && beforePanel <= 7) {
							int afterPanel = beforePanel + 1;

							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}

							int uiPointArray[] = getUiPoint(afterPanel);

							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}

						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});

			addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					isSelected = true;

					float fixedRatio = 0.1f;
					if (e.getWheelRotation() < 0) {
						ratioW *= (1 + fixedRatio);
						ratioH *= (1 + fixedRatio);
					} else if (e.getWheelRotation() > 0) {
						ratioW *= (1 - fixedRatio);
						ratioH *= (1 - fixedRatio);
					}

					updatePanelSize();

					DraggablePanel c = DraggablePanel.this;

					int subW = (int) (c.currentWidth > (c.currentWidth * ratioW)
							? c.currentWidth - (c.currentWidth * ratioW)
							: (c.currentWidth * ratioW) - c.currentWidth);
					int subH = (int) (c.currentHeight > (c.currentHeight * ratioH)
							? c.currentHeight - (c.currentHeight * ratioH)
							: (c.currentHeight * ratioH) - c.currentHeight);
					dropSendSizeChangeXML(DraggablePanel.this.getName(), panelResult, ratioW, ratioH, subW, subH);
				}

			});

		}

		private void updatePanelSize() {
			int newWidth = (int) (originalWidth * ratioW);
			int newHeight = (int) (originalHeight * ratioH);

			// 현재 크기를 업데이트합니다.
			currentWidth = newWidth;
			currentHeight = newHeight;

			this.setSize(newWidth, newHeight);

			resizeImage(newWidth, newHeight);

			label.setSize(new Dimension(newWidth, newHeight));
			createHitBoxes();

			this.revalidate();
			this.repaint();
		}

		private void createHitBoxes() {
			if (!isSelected) {
				return;
			}

			int w = currentWidth;
			int h = currentHeight;
			hitBoxes.clear();
			// 모서리에 조절점 추가
			hitBoxes.add(new Rectangle(0, 0, cornerSize, cornerSize)); // 좌상단
			hitBoxes.add(new Rectangle(w - cornerSize, 0, cornerSize, cornerSize)); // 우상단
			hitBoxes.add(new Rectangle(0, h - cornerSize, cornerSize, cornerSize)); // 좌하단
			hitBoxes.add(new Rectangle(w - cornerSize, h - cornerSize, cornerSize, cornerSize)); // 우하단
			// 가장자리 중앙에 조절점 추가
			hitBoxes.add(new Rectangle(w / 2 - cornerSize / 2, 0, cornerSize, cornerSize)); // 상단 중앙
			hitBoxes.add(new Rectangle(w / 2 - cornerSize / 2, h - cornerSize, cornerSize, cornerSize)); // 하단 중앙
			hitBoxes.add(new Rectangle(0, h / 2 - cornerSize / 2, cornerSize, cornerSize)); // 왼쪽 중앙
			hitBoxes.add(new Rectangle(w - cornerSize, h / 2 - cornerSize / 2, cornerSize, cornerSize)); // 오른쪽 중앙

			// hitBoxPanel의 크기와 위치를 업데이트합니다.
			if (hitBoxPanel != null) {
				hitBoxPanel.setHitBoxes(hitBoxes);
				hitBoxPanel.setSize(new Dimension(w, h));
				hitBoxPanel.revalidate();
				hitBoxPanel.repaint();
//              hitBoxPanel.setHitBoxes(hitBoxes);
//              hitBoxPanel.setBounds(0, 0, w, h);
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			if (isSelected) {
				// System.out.println("hi");
				g2d.setColor(Color.RED);
				for (Rectangle rect : hitBoxes) {
					g2d.fill(rect);
				}
			}
		}

		public void resizeImage(int width, int height) {
			Image resizedImg = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(resizedImg));
		}

		private void resizePanel(Point p) {
			int dx = p.x - initialClick.x;
			int dy = p.y - initialClick.y;
			int newWidth = getWidth();
			int newHeight = getHeight();
			// 선택된 조절점에 따라 패널 크기 조절 로직 구현
			if (hitBoxIndex == 0) { // 좌상단
				newWidth = getWidth() - dx;
				newHeight = getHeight() - dy;
				setBounds(getX() + dx, getY() + dy, newWidth, newHeight);
			} else if (hitBoxIndex == 1) { // 우상단
				newWidth = getWidth() + dx;
				newHeight = getHeight() - dy;
				setBounds(getX(), getY() + dy, newWidth, newHeight);
			} else if (hitBoxIndex == 2) { // 좌하단
				newWidth = getWidth() - dx;
				newHeight = getHeight() + dy;
				setBounds(getX() + dx, getY(), newWidth, newHeight);
			} else if (hitBoxIndex == 3) { // 우하단
				newWidth = getWidth() + dx;
				newHeight = getHeight() + dy;
				setBounds(getX(), getY(), newWidth, newHeight);
			}
			// 상단 중앙 조절점 선택 시
			else if (hitBoxIndex == 4) {
				newHeight = getHeight() - dy;
				setBounds(getX(), getY() + dy, getWidth(), newHeight); // 변수별로 받아서 x,y
			}
			// 하단 중앙 조절점 선택 시
			else if (hitBoxIndex == 5) {
				newHeight = getHeight() + dy;
				setBounds(getX(), getY(), getWidth(), newHeight);
			}
			// 왼쪽 중앙 조절점 선택 시
			else if (hitBoxIndex == 6) {
				newWidth = getWidth() - dx;
				setBounds(getX() + dx, getY(), newWidth, getHeight());
			}
			// 오른쪽 중앙 조절점 선택 시
			else if (hitBoxIndex == 7) {
				newWidth = getWidth() + dx;
				setBounds(getX(), getY(), newWidth, getHeight());
			}

			setBounds(getX(), getY(), newWidth, newHeight);
			label.setSize(newWidth, newHeight);
			resizeImage(newWidth, newHeight);
			currentWidth = newWidth;
			currentHeight = newHeight;

			ratioW = currentWidth / originalWidth;
			ratioH = currentHeight / originalHeight;
			createHitBoxes();
			hitBoxPanel.setBounds(0, 0, currentWidth, currentHeight);

			dropSendSizeChangeXML(this.getName(), panelResult, ratioW, ratioH, 0, 0);
			// hitBoxes.clear();
			// repaint();
			revalidate();
		}

		private void dropSendSizeChangeXML(String id, int panelNumber, float currentWidthRatio,
				float currentHeightRatio, int subW, int subH) {
			try {

				int[] pointArray = getXYPoint(panelNumber);

				String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
				xmlString += "<Commands>";
				xmlString += "<command type=\"change\">";
				xmlString += "<id>" + id + "</id>";

				if (panelNumber == 3) {
					xmlString += "<x>" + (pointArray[0] * currentWidthRatio) + "</x>";
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 2) {
					xmlString += "<x>" + (pointArray[0] * (2 - currentWidthRatio)) + "</x>";
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 6) {
					System.out.println(currentWidthRatio);
					xmlString += "<x>" + (pointArray[0] * (2 - currentWidthRatio)) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + ((pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 7) {
					System.out.println(currentWidthRatio);
					xmlString += "<x>" + (pointArray[0] * currentWidthRatio) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + (pointArray[1] * currentHeightRatio) + "</y>";
				}
				if (panelNumber == 1) {
					System.out.println(currentWidthRatio);
					xmlString += "<x>" + (pointArray[0] + (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}

				if (panelNumber == 5) {

					xmlString += "<x>" + (pointArray[0] + (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					xmlString += "<y>" + ((pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 4) {

					xmlString += "<x>" + (pointArray[0] - (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";

				}
				if (panelNumber == 8) {

					xmlString += "<x>" + (pointArray[0] - (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					xmlString += "<y>" + ((pointArray[1] * currentHeightRatio)) + "</y>";

				}

				xmlString += "<boundsfill>1</boundsfill>";
				xmlString += "<boundsh>" + (1080 * currentHeightRatio) + "</boundsh>";
				xmlString += "<boundsw>" + (1927 * currentWidthRatio) + "</boundsw>";
				xmlString += "</command>";
				xmlString += "</Commands>";

				URL requestURL = new URL(baseURL + "/xmlcommand");
				HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
				requestConnection.setRequestMethod("POST");
				requestConnection.setDoOutput(true);
				OutputStream reqStream = requestConnection.getOutputStream();
				reqStream.write(xmlString.getBytes("UTF8"));

				if (requestConnection.getResponseCode() == 200) {
//               System.out.println(
//                     "Success to drag sizechange " + id + " response : " + requestConnection.getResponseCode());

					for (openFileList item : openFileList) {
						if (item.id.equals(id)) {
							item.panelNumber = panelNumber;
						}
					}

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							DraggablePanel selectedPanel = null;
							for (DraggablePanel panel : dragPanelOpenList) {
								if (panel.getName().equals(id)) {
									selectedPanel = panel;
								}
							}

							int uiPointArray[] = getUiPoint(panelNumber);

							selectedPanel.setBounds(uiPointArray[0], uiPointArray[1],
									(int) (selectedPanel.getPreferredSize().width * currentWidthRatio),
									(int) (selectedPanel.getPreferredSize().height * currentHeightRatio));
							selectedPanel.revalidate();
						}
					});
				} else
					System.out.println(
							"Failed to drag change " + id + " response: " + requestConnection.getResponseCode());
			} catch (Exception e) {
				System.out.println("Failed to drag open error: " + e);
			}
		}

	}

	private void sendVideoMute(String id) {
		try {
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
			xmlString += "<Commands>";
			xmlString += "<command type=\"change\">";
			xmlString += "<id>" + id + "</id>";
			xmlString += "<mute>True<mute>";
			xmlString += "</command>";
			xmlString += "</Commands>";

			URL requestURL = new URL(baseURL + "/xmlcommand");
			HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
			requestConnection.setRequestMethod("POST");
			requestConnection.setDoOutput(true);
			OutputStream reqStream = requestConnection.getOutputStream();
			reqStream.write(xmlString.getBytes("UTF8"));

			if (requestConnection.getResponseCode() == 200) {
				System.out.println("소리 끄기 성공 / 응답코드 : " + requestConnection.getResponseCode());
			} else
				JOptionPane.showMessageDialog(frame, "음소거에 실패하였습니다\n응답코드: " + requestConnection.getResponseCode(),
						"Warning", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (e != null && e.toString().contains("Connection refused")) {
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다. 컨트롤러가 켜져있는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다. IP와 PORT 번호가 맞는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection Exception")) {
				JOptionPane.showMessageDialog(frame, "연결하는데 오류가 발생했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "오류로 인해 음소거를 하지 못했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void sendVideoVolume(String id, int volume) {

		try {
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
			xmlString += "<Commands>";
			xmlString += "<command type=\"change\">";
			xmlString += "<id>" + id + "</id>";
			xmlString += "<volume>" + volume + "</volume>";
			xmlString += "</command>";
			xmlString += "</Commands>";

			URL requestURL = new URL(baseURL + "/xmlcommand");
			HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
			requestConnection.setRequestMethod("POST");
			requestConnection.setDoOutput(true);
			OutputStream reqStream = requestConnection.getOutputStream();
			reqStream.write(xmlString.getBytes("UTF8"));

			if (requestConnection.getResponseCode() == 200) {
				System.out.println(
						"Success to adjust Volume" + id + " response : " + requestConnection.getResponseCode());
			} else
				JOptionPane.showMessageDialog(frame, "소리 조절에 실패하였습니다\n응답코드: " + requestConnection.getResponseCode(),
						"Warning", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (e != null && e.toString().contains("Connection refused")) {
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다. 컨트롤러가 켜져있는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다. IP와 PORT 번호가 맞는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection Exception")) {
				JOptionPane.showMessageDialog(frame, "연결하는데 오류가 발생했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "오류로 인해 소리 조절에 실패했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void sendDeleteXML(String id) {
		try {
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
			xmlString += "<Commands>";
			xmlString += "<command type=\"close\">";
			xmlString += "<id>" + id + "</id>";
			xmlString += "</command>";
			xmlString += "</Commands>";

			URL requestURL = new URL(baseURL + "/xmlcommand");
			HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
			requestConnection.setRequestMethod("POST");
			requestConnection.setDoOutput(true);
			OutputStream reqStream = requestConnection.getOutputStream();
			reqStream.write(xmlString.getBytes("UTF8"));

			if (requestConnection.getResponseCode() == 200) {
				System.out.println("Success to close " + id + " response : " + requestConnection.getResponseCode());
			} else
				JOptionPane.showMessageDialog(frame, "컨텐츠 삭제에 실패하였습니다\n응답코드 : " + requestConnection.getResponseCode(),
						"Warning", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (e != null && e.toString().contains("Connection refused")) {
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다. 컨트롤러가 켜져있는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다. IP와 PORT 번호가 맞는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection Exception")) {
				JOptionPane.showMessageDialog(frame, "연결하는데 오류가 발생했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "오류로 인해 컨텐츠 삭제에 실패하였습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

	}

	private void dropSendChangeXML(String id, int panelNumber) {
		try {

			int[] pointArray = getXYPoint(panelNumber);

			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
			xmlString += "<Commands>";
			xmlString += "<command type=\"change\">";
			xmlString += "<id>" + id + "</id>";
			xmlString += "<x>" + pointArray[0] + "</x>";
			xmlString += "<y>" + pointArray[1] + "</y>";
			xmlString += "<boundsfill>1</boundsfill>";
			xmlString += "<boundsh>1080</boundsh>";
			xmlString += "<boundsw>1927</boundsw>";
			xmlString += "</command>";
			xmlString += "</Commands>";

			URL requestURL = new URL(baseURL + "/xmlcommand");
			HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
			requestConnection.setRequestMethod("POST");
			requestConnection.setDoOutput(true);
			OutputStream reqStream = requestConnection.getOutputStream();
			reqStream.write(xmlString.getBytes("UTF8"));

			if (requestConnection.getResponseCode() == 200) {
				System.out
						.println("Success to drag change " + id + " response : " + requestConnection.getResponseCode());

				for (openFileList item : openFileList) {
					if (item.id.equals(id)) {
						item.panelNumber = panelNumber;
					}
				}

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						DraggablePanel selectedPanel = null;
						for (DraggablePanel panel : dragPanelOpenList) {
							if (panel.getName().equals(id)) {
								selectedPanel = panel;
							}
						}

						int uiPointArray[] = getUiPoint(panelNumber);

						selectedPanel.setBounds(uiPointArray[0], uiPointArray[1], selectedPanel.currentWidth,
								selectedPanel.currentHeight);
						selectedPanel.revalidate();
					}
				});
			} else
				JOptionPane.showMessageDialog(frame, "위치변경에 실패하였습니다\n응답코드 : " + requestConnection.getResponseCode(),
						"Warning", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (e != null && e.toString().contains("Connection refused")) {
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다. 컨트롤러가 켜져있는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다. IP와 PORT 번호가 맞는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection Exception")) {
				JOptionPane.showMessageDialog(frame, "연결하는데 오류가 발생했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "오류로 인해 컨텐츠 위치변경에 실패하였습니다.\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void dropSendOpenXML(String name, int panelNumber, String filePath) {
		try {
			boolean fileSearchResult = false;
			int fileIndexSearchResult = 0;
			for (int idx = 0; idx < controllerFileList.size(); idx++) {
				fileSearchResult = controllerFileList.get(idx).contains(name);
				if (fileSearchResult) {
					fileIndexSearchResult = idx;
					break;
				}
			}
//			String openDir = controllerFileList.get(fileIndexSearchResult).replace("\\\\192.168.1.128\\contents\\", "");
			String openDir = controllerFileList.get(fileIndexSearchResult).replace(rootPath + '\\', "");

			int[] pointArray = getXYPoint(panelNumber);

			String id = Integer.toString(panelNumber);

			if (fileSearchResult) {

				String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>";
				xmlString += "<Commands>";
				xmlString += "<command type=\"open\">";
				xmlString += "<id>" + id + "</id>";
				xmlString += "<name>" + openDir + "</name>";
				xmlString += "<x>" + pointArray[0] + "</x>";
				xmlString += "<y>" + pointArray[1] + "</y>";
				xmlString += "<boundsfill>1</boundsfill>";
				xmlString += "<boundsh>1080</boundsh>";
				xmlString += "<boundsw>1927</boundsw>";
				xmlString += "</command>";
				xmlString += "</Commands>";

				URL requestURL = new URL(baseURL + "/xmlcommand");
				HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
				requestConnection.setRequestMethod("POST");
				requestConnection.setDoOutput(true);
				OutputStream reqStream = requestConnection.getOutputStream();
				reqStream.write(xmlString.getBytes("UTF8"));

				int code = requestConnection.getResponseCode();

				if (code == 200) {
					System.out.println(
							"Success to drag open " + openDir + " response : " + requestConnection.getResponseCode());

					openFileList.add(new openFileList(name, id, panelNumber));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							DraggablePanel draggablePanel = new DraggablePanel(filePath);
							draggablePanel.setOpaque(false);
							draggablePanel.setName(id);
							dragPanelOpenList.add(draggablePanel);

							layeredPane = frame.getLayeredPane();

							int uiPointArray[] = getUiPoint(panelNumber);

							layeredPane.add(draggablePanel, JLayeredPane.DEFAULT_LAYER);
							draggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
									draggablePanel.getPreferredSize().width, draggablePanel.getPreferredSize().height);
							draggablePanel.revalidate();
						}
					});
				} else
					JOptionPane.showMessageDialog(frame, openDir + "컨텐츠 열기에 실패하였습니다\n응답코드 : " + code, "Warning",
							JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			if (e != null && e.toString().contains("Connection refused")) {
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다. 컨트롤러가 켜져있는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다. IP와 PORT 번호가 맞는지 확인하세요\n" + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection Exception")) {
				JOptionPane.showMessageDialog(frame, "연결하는데 오류가 발생했습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "오류로 인해 컨텐츠 열기에 실패하였습니다\nError : " + e, "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private int[] getUiPoint(int panelNumber) {
		int[] result = { 0, 0 };

		if (1 <= panelNumber && panelNumber <= 4) {
			result[0] = 290 + 204 * (panelNumber - 1);
			result[1] = 262;
		} else if (5 <= panelNumber && panelNumber <= 8) {
			result[0] = 290 + 204 * (panelNumber - 5);
			result[1] = 381;
		}
		return result;

	}

	private int[] getXYPoint(int panelNumber) {
		int[] result = { 0, 0 };

		if (panelNumber == 1) {
			result[0] = -2880;
			result[1] = 540;
		} else if (panelNumber == 2) {
			result[0] = -960;
			result[1] = 540;
		} else if (panelNumber == 3) {
			result[0] = 960;
			result[1] = 540;
		} else if (panelNumber == 4) {
			result[0] = 2880;
			result[1] = 540;
		} else if (panelNumber == 5) {
			result[0] = -2880;
			result[1] = -540;
		} else if (panelNumber == 6) {
			result[0] = -960;
			result[1] = -540;
		} else if (panelNumber == 7) {
			result[0] = 960;
			result[1] = -540;
		} else if (panelNumber == 8) {
			result[0] = 2880;
			result[1] = -540;
		}

		return result;
	}

	public DefaultMutableTreeNode getFile(String path) {
		File file = new File(path);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName());

		if (file.isDirectory()) {

			File[] files = file.listFiles();
			if (files.length == 0) {
				root.add(new DefaultMutableTreeNode(file.getName()));
			}
			if (files != null) {
				for (File child : files) {
					if (!child.toString().endsWith(".hwc") && !child.toString().endsWith(".hws")
							&& !child.toString().endsWith(".hwtxt") && !child.toString().contains("HiperwallAnimations")
							&& !child.toString().contains("hiperwall_thumbs")
							&& !child.toString().contains("schedules")) {
						// System.out.println(child.toString());
						root.add(getFile(child.getAbsolutePath()));
						if (child.toString().contains(".")) {
							controllerFileList.add(child.toString());
						} else if (!child.toString().endsWith(".")) {
							String folderName = child.toString().replace(rootPath + "\\", "");

							folderNameList.add(folderName);

						}
					}
				}
			}
		}

		return root;
	}

	public static void main(String[] args) {
		new Controller_main();
	}

}

class openFileList {
	String name;
	String id;
	int panelNumber;

	public openFileList(String nameStr, String idStr, int panelResult) {
		name = nameStr;
		id = idStr;
		panelNumber = panelResult;
	}
}