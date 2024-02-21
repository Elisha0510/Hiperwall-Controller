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

		// 사용자가 확인 버튼을 눌렀을 때 true 반환
		if (result == JOptionPane.OK_OPTION) {
			return true;
		}
		// 사용자가 취소 버튼을 눌렀을 때 false 반환
		return false;
	}

	private String makeURL() {
		// ip와 port를 가지고 통신을 보낼 URL 생성
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

	// 마우스 X, Y 좌표로 비디오 월 번호 반환
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

	// 드래그 자체를 인식하는 이벤트
	private class FileDragGestureListener implements DragGestureListener {
		@Override
		public void dragGestureRecognized(DragGestureEvent dge) {
			StringSelection transferable = null;
			// 파일을 눌렀을 때 선택한 파일에 대한 경로 반환
			TreePath path = tree.getSelectionPath();

			// 선택한 것이 파일이 아닌 경우
			if (path != null) {
				transferable = new StringSelection(path.getLastPathComponent().toString());
			}
			// 선택한 것이 파일인 경우
			else if (path == null) {
				Component draggedComponent = dge.getComponent();

				// 선택한 것이 DraggablePanel인 경우
				if (draggedComponent instanceof DraggablePanel) {
					DraggablePanel draggablePanel = (DraggablePanel) draggedComponent;
					// 선택한 DraggablePanel의 id값 구하기
					String idValue = draggablePanel.getName();

					// id값을 drop 이벤트가 인식할 수 있도록 StringSelection으로 변환
					transferable = new StringSelection(idValue);
				}
			}
			// drop 이벤트에게 transferable 데이터를 보냄
			dge.startDrag(null, transferable, new FileDragSourceListener());
		}

	}

	// 드래그 하는 동안을 인식하는 이벤트
	private class FileDragSourceListener implements DragSourceListener {
		@Override
		public void dragEnter(DragSourceDragEvent dsde) {

		}

		@Override
		public void dragOver(DragSourceDragEvent dsde) {

			// 드래그를 한 객체를 불러오기
			Component draggedComponent = dsde.getDragSourceContext().getComponent();
			DraggablePanel selectPanel = null;

			// 드래그를 한 객체가 DraggablePanel인 경우
			if (draggedComponent instanceof DraggablePanel) {
				DraggablePanel draggablePanel = (DraggablePanel) draggedComponent;

				// DraggablePanel의 id 값을 구함
				String idValue = draggablePanel.getName();

				// 비디오 월에 열린 DraggablePanel 중에 그 id를 가진 객체가 있는지 확인
				for (DraggablePanel panel : dragPanelOpenList) {
					if (panel.getName().equals(idValue)) {
						selectPanel = panel;
						break;
					}
				}
			}
			// 검사 후 일치한 객체가 있는 경우
			if (selectPanel != null) {
				// 화면의 좌표를 가지고 옴
				Point locationOnScreen = frame.getLocationOnScreen();
				// 화면의 좌표 중 x, y 값을 가지고 옴
				int x = (int) locationOnScreen.getX();
				int y = (int) locationOnScreen.getY();
				// 화면에 일치한 객체를 각 좌표에 맞게 띄움
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

		// drop 하는 이벤트
		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
				// drag할 때 보냈던 데이터 변환
				Transferable transferable = dtde.getTransferable();
				DataFlavor[] flavors = transferable.getTransferDataFlavors();

				if (flavors.length > 0) {

					// drag 할 때 보냈던 데이터 문자열로 변환
					Object data = transferable.getTransferData(flavors[0]);
					String name = data.toString();

					DraggablePanel draggablePanel = null; // dropSendSizeChangeXML을 사용하기 위해 필요

					// 비디오 월에 열린 DraggablePanel 중 해당 데이터를 가진 객체를 검사
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

					// drop 한 위치값의 x, y 좌표 가지고 오기
					Point point = dtde.getLocation();
					int pointX = (int) point.getX();
					int pointY = (int) point.getY();

					// x, y 좌표로 비디오 월의 번호 구하기
					panelResult = getPanelNumber(pointX, pointY);

					// 비디오 월에 이미 열린 객체 인 경우
					if (openedResult && draggablePanel != null) {
						// 객체 위치 변경
						draggablePanel.dropSendSizeChangeXML(name, panelResult, draggablePanel.ratioW,
								draggablePanel.ratioH, 0, 0);

					}
					// 비디오 월에 아직 열리지 않은 경우
					else if (!openedResult) {

						boolean fileSearchResult = false;
						int fileIndexSearchResult = -1;
						// 전체 파일에서 열고 싶은 파일 찾기
						for (int idx = 0; idx < controllerFileList.size(); idx++) {
							fileSearchResult = controllerFileList.get(idx).contains(name);
							if (fileSearchResult) {
								fileIndexSearchResult = idx;
								break;
							}
						}
						// 전체 파일에 열고 싶은 파일이 있는 경우
						if (fileSearchResult) {
							// 파일 경로 받아오기
							String filePath = controllerFileList.get(fileIndexSearchResult);
							// 파일 열기
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

	class HitBoxPanel extends JPanel {
		private ArrayList<Rectangle> hitBoxes;

		public HitBoxPanel(ArrayList<Rectangle> hitBoxes) {
			this.hitBoxes = new ArrayList<>(hitBoxes); // HitBoxPanel이 생성 시 호출되어 인수 hitBoxes 리스트로 클래스 멤버변수 초기화
			setOpaque(false);
		}

		public void setHitBoxes(ArrayList<Rectangle> hitBoxes) {
			this.hitBoxes = hitBoxes; // 새로운 hitBoxes 리스트로 멤버 변수 업데이트
			repaint(); // 호출되면 paintComponent로 새로운 hitBoxes로 다시 그리기
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g; // 더 세밀한 설정을 위해 Graphics 객체를 Graphics2D로 캐스팅
			g2d.setColor(Color.RED);
			for (Rectangle rect : hitBoxes) {
				g2d.fill(rect); // 모든 hitBoxes 순회해서 Rectangle 빨간색으로 채우기
			}
		}
	}

	class DraggablePanel extends JPanel {
		Point initialClick; // 드래그 시작 지점
		JLabel label;
		Component sourceComponent; // 이벤트가 시작되는 컴포넌트
		boolean pressed_flag; // key event 발생 여부 확인하는 플래그

		private final int cornerSize = 10; // 조절점의 크기
		private ArrayList<Rectangle> hitBoxes = new ArrayList<>();
		private int hitBoxIndex = -1; // 선택된 조절점이 없을 때 -1
		private boolean isSelected = false; // 객체가 선택(클릭)되었는지 확인해주는 플래그
		private HitBoxPanel hitBoxPanel; // 조절점을 표시하는 패널
		private int currentWidth, currentHeight;
		private float ratioW, ratioH;
		private float originalWidth, originalHeight;
		private Image originalImage;

		public DraggablePanel(String text) {
			setLayout(new BorderLayout());

			currentWidth = 204;// 월 한 칸 크기 만큼
			currentHeight = 119; // 기본 크기
			ratioW = 1.0f;
			ratioH = 1.0f;
			originalWidth = currentWidth; // 204
			originalHeight = currentHeight; // 119

			ImageIcon imageIcon = new ImageIcon(text); // text로 이미지 경로 가져와서 imageIcon 생성
			originalImage = imageIcon.getImage(); // 불러온 원본 이미지를 변수에 저장
			Image scaledImage = originalImage.getScaledInstance(currentWidth, currentHeight, Image.SCALE_SMOOTH); // 가져올
																													// 이미지를
																													// 월
																													// 한
																													// 칸
																													// 크기로
																													// 조절
			ImageIcon scaledIcon = new ImageIcon(scaledImage); // 크기 조절한 이미지를 바탕으로 ImageIcon 생성
			this.label = new JLabel(scaledIcon); // label에 사진 붙여주기

			this.label.setBounds(0, 0, currentWidth, currentHeight);// 이미지아이콘을 넣은 label 크기를 월 한 칸 크기로 조절

			createHitBoxes(); // 조절점을 나타내는 hitBoxes 리스트 초기화해주기
			hitBoxPanel = new HitBoxPanel(hitBoxes); // 변수를 HitBoxPanel 객체로 초기화
			hitBoxPanel.setBounds(0, 0, currentWidth, currentHeight); // 조절점도 draggablePanel 크기(월 1칸)에 맞도록 설정

			JLayeredPane layeredPane = new JLayeredPane(); // 겹치는 JLabel, hitBoxPanel 컴포넌트를 관리해주기 위해 사용
			layeredPane.setPreferredSize(new Dimension(currentWidth, currentHeight)); // 지정한 크기로 내부에 추가되는 컴포넌트들이 올바르게
																						// 표시되도록 설정
			layeredPane.add(this.label, Integer.valueOf(0)); // JLabel을 기본 레이어에 추가
			layeredPane.add(hitBoxPanel, Integer.valueOf(1)); // HitBoxPanel을 레이어 1에 추가

			this.add(layeredPane, BorderLayout.CENTER); // LayeredPane을 DraggablePanel의 중앙에 추가해서 레이아웃 관리

			DragSource dragSource = DragSource.getDefaultDragSource();
			dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY,
					new FileDragGestureListener()); // 드래그 제스처 리스너를 등록해서 사용자가 드래그를 시작할 때 호출

			addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					Component c = (Component) e.getSource();
					c.setFocusable(true);
					c.requestFocus();

					if (c instanceof DraggablePanel) { // 클릭된 컴포넌트가(c) draggablePanel인지 확인하기
						DraggablePanel addPanel = (DraggablePanel) c;
						clickPanel.add(addPanel); // 조건이 성립하면 DraggablePanel을 clickPanel 리스트에 추가

						initialClick = e.getPoint(); // 마우스 클릭을 시작한 초기 위치 저장

						for (int i = 0; i < hitBoxes.size(); i++) { // hitBoxes에 저장된 모든 조절점 순회하기
							if (hitBoxes.get(i).contains(initialClick)) { // 클릭 위치가 조절점 안에 있는지 확인하기
								hitBoxIndex = i; // 있다면 해당 조절점의 index 저장하기
								isSelected = true; // 조절점 클릭된 상태 변경하기
								revalidate();// 컴포넌트 크기, 레이아웃 계산하기
								return;
							}
						}
						if (!isSelected) {
							isSelected = true;
							createHitBoxes(); // 조절점 생성하기
							repaint();
						} else if (isSelected) {
							isSelected = false;
							hitBoxes.clear(); // 조절점 삭제하기
							repaint();
						}
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getComponent() instanceof DraggablePanel) { // 이벤트 발생한 컴포넌트가 DraggablePanel인지 확인하기
						DraggablePanel releasedPanel = (DraggablePanel) e.getComponent(); // releasedPanel = 이벤트가 발생한
																							// 컴포넌트
						releasedPanel.setSize(releasedPanel.currentWidth, releasedPanel.currentHeight); // 크기 조절을 끝낸 최종
																										// 컴포넌트 크기 갱신
						releasedPanel.label.setSize(releasedPanel.currentWidth, releasedPanel.currentHeight); // 크기 조절을
																												// 끝낸 최종
																												// 컴포넌트의
																												// 이미지
																												// 크기 갱신
						revalidate();
						hitBoxIndex = -1;
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {

					Component component = e.getComponent();
					if (component instanceof DraggablePanel) { // 이벤트 발생한 컴포넌트가 DraggablePanel인지 확인하기
						DraggablePanel draggedPanel = (DraggablePanel) component;

						initialClick = e.getPoint();

						int deltaX = e.getX() - initialClick.x; // 초기 클릭과 현재 위치의 차이를 계산하기
						int deltaY = e.getY() - initialClick.y;

						if (hitBoxIndex != -1 && isSelected) { // 조절점과 컴포넌트 모두 선택된 경우: 크기 조절
							resizePanel(e.getPoint()); // 패널 크기 재설정하기
						} else if (hitBoxIndex == -1 && isSelected) { // 조절점이 선택되지 않고 컴포넌트만 선택된 경우: 위치 이동
							int newX = draggedPanel.getX() + deltaX;
							int newY = draggedPanel.getY() + deltaY;
							draggedPanel.setLocation(newX, newY); // 드래그중인 패널의 새 위치로 이동
						}

						for (DraggablePanel panel : dragPanelOpenList) {
							if (panel.getName().equals(draggedPanel.getName())) {
								panel.setSize(draggedPanel.getWidth(), draggedPanel.getHeight()); // dragPanelOpenList를
																									// 전부 순회해서 해당 패널을 찾아
																									// 속성 업데이트
								break;
							}
						}
						createHitBoxes(); // 속성 업데이트를 끝내면 다시 조절점 갱신하기
						repaint();
					}
				}
			});

			addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				// 키보드에 대한 이벤트
				@Override
				public void keyPressed(KeyEvent e) {

					// 클릭을 하고 Delete 버튼을 눌렀을 때
					if (e.getKeyCode() == KeyEvent.VK_DELETE && isSelected) {
						// 클릭을 한 Component를 가지고 옴
						Component pressedComponent = e.getComponent();
						JLayeredPane layeredPane = frame.getLayeredPane();

						// 그 Component가 DraggablePanel일 때
						if (pressedComponent instanceof DraggablePanel) {
							DraggablePanel draggablePanel = (DraggablePanel) pressedComponent;
							// id 값 가져오기
							String idValue = draggablePanel.getName();

							// 열려있는 DraggablePanel 중 해당 id값을 가진 것을 찾음
							for (DraggablePanel panel : dragPanelOpenList) {
								if (panel.getName().equals(idValue)) {
									// 하이퍼월 컨트롤러에서 그 DraggablePanel 지우기
									sendDeleteXML(idValue);
									// dragPanelOpenList에서 panel 지우기
									layeredPane.remove(pressedComponent);
									layeredPane.repaint();
									dragPanelOpenList.remove(panel);
									// 클릭 초기화
									pressed_flag = false;
									break;
								}
							}
						}
					}

					// 클릭한 후 아래 방향키를 눌렀을 때
					if (e.getKeyCode() == KeyEvent.VK_DOWN && isSelected) {

						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						if (!clickPanel.isEmpty()) {
							// 클릭한 Panel 가져오기
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						// 클릭한 Panel의 panelNumber과 id값을 가져오기
						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}
						// 비디오 월 번호가 1 이상 4 이하 일 때
						if (1 <= beforePanel && beforePanel <= 4) {
							// 이동 후 비디오 월 번호 계산
							int afterPanel = beforePanel + 4;
							// 이동에 따른 객체 change 명령 보내기
							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}
							// 이동 후에 대한 통합 컨트롤러 비디오 월 좌표 구하기
							int uiPointArray[] = getUiPoint(afterPanel);

							// UI상 객체의 좌표를 변경해 객체 이동
							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}

						}

					}

					// 클릭한 후 위 방향키를 눌렀을 때
					if (e.getKeyCode() == KeyEvent.VK_UP && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						// 선택한 객체가 있을 때
						if (!clickPanel.isEmpty()) {
							// 선택한 마지막 객체 가지고 오기
							currentDraggablePanel = clickPanel.get(lastIndex);
						}
						// 열린 파일 중 선택한 객체를 검사
						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}
						// 비디오 월의 번호가 5 이상 8 이하 일 때
						if (5 <= beforePanel && beforePanel <= 8) {
							// 이동 후 비디오 월 번호 계산
							int afterPanel = beforePanel - 4;
							// 이동에 따른 객체 change 명령 보내기
							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}
							// 이동 후에 대한 통합 컨트롤러 비디오 월 좌표 구하기
							int uiPointArray[] = getUiPoint(afterPanel);
							// UI상 객체의 좌표를 변경해 객체 이동
							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}
						}
					}

					if (e.getKeyCode() == KeyEvent.VK_LEFT && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						// 선택한 객체가 있을 때
						if (!clickPanel.isEmpty()) {
							// 선택한 마지막 객체 가지고 오기
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						// 열린 파일 중 선택한 객체를 검사
						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

						if (2 <= beforePanel && beforePanel <= 4 || 6 <= beforePanel && beforePanel <= 8) {
							// 이동 후 비디오 월 번호 계산
							int afterPanel = beforePanel - 1;
							// 이동에 따른 객체 change 명령 보내기
							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}
							// 이동 후에 대한 통합 컨트롤러 비디오 월 좌표 구하기
							int uiPointArray[] = getUiPoint(afterPanel);
							// UI상 객체의 좌표를 변경해 객체 이동
							if (currentDraggablePanel != null) {
								currentDraggablePanel.setBounds(uiPointArray[0], uiPointArray[1],
										currentDraggablePanel.getPreferredSize().width,
										currentDraggablePanel.getPreferredSize().height);
								currentDraggablePanel.revalidate();
							}
						}
					}

					if (e.getKeyCode() == KeyEvent.VK_RIGHT && isSelected) {
						int lastIndex = clickPanel.size() - 1;
						int beforePanel = 0;
						String panelID = "";

						DraggablePanel currentDraggablePanel = null;
						// 선택한 객체가 있을 때
						if (!clickPanel.isEmpty()) {
							// 선택한 마지막 객체 가지고 오기
							currentDraggablePanel = clickPanel.get(lastIndex);
						}

						// 열린 파일 중 선택한 객체를 검사
						for (openFileList item : openFileList) {
							if (item.id.equals(currentDraggablePanel.getName())) {
								beforePanel = item.panelNumber;
								panelID = item.id;
							}
						}

						if (1 <= beforePanel && beforePanel <= 3 || 5 <= beforePanel && beforePanel <= 7) {
							// 이동 후 비디오 월 번호 계산
							int afterPanel = beforePanel + 1;
							// 이동에 따른 객체 change 명령 보내기
							if (currentDraggablePanel != null) {
								dropSendSizeChangeXML(panelID, afterPanel, currentDraggablePanel.ratioW,
										currentDraggablePanel.ratioH, 0, 0);
							}
							// 이동 후에 대한 통합 컨트롤러 비디오 월 좌표 구하기
							int uiPointArray[] = getUiPoint(afterPanel);
							// UI상 객체의 좌표를 변경해 객체 이동
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

			addMouseWheelListener(new MouseWheelListener() { // 스크롤이 움직이는 이벤트를 받으면 크기 변경이 발생

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					isSelected = true; // 크기조절 할 객체가 선택되면 플래그도 변경하기

					float fixedRatio = 0.1f; // 스크롤 한 번당 변경될 크기 비율
					if (e.getWheelRotation() < 0) { // 업스크롤 하는 이벤트가 발생하는 경우 = 확대 시
						ratioW *= (1 + fixedRatio);
						ratioH *= (1 + fixedRatio);
					} else if (e.getWheelRotation() > 0) { // 다운스크롤 하는 이벤트가 발생하는 경우 = 축소 시
						ratioW *= (1 - fixedRatio);
						ratioH *= (1 - fixedRatio);
					}

					updatePanelSize(); // 변경된 값을 반영하여 패널 사이즈 업데이트 해주기

					DraggablePanel c = DraggablePanel.this;
					// currentWidth와 currentHeight가 스크롤을 통해 변경될 새로운 너비와 높이(currentWidth * ratioW,
					// currentHeight * ratioH)와 어떻게 다른지를 계산하는 변수
					int subW = (int) (c.currentWidth > (c.currentWidth * ratioW)
							? c.currentWidth - (c.currentWidth * ratioW) // 패널 너비가 변화된 차이를 절대값으로 계산하기
							: (c.currentWidth * ratioW) - c.currentWidth);
					int subH = (int) (c.currentHeight > (c.currentHeight * ratioH)
							? c.currentHeight - (c.currentHeight * ratioH) // 패널 높이가 변화된 차이를 절대값으로 계산하기
							: (c.currentHeight * ratioH) - c.currentHeight);
					// subW, subH: 크기 조절된 패널의 위치와 크기를 업데이트 할 때 필요한 정보
					dropSendSizeChangeXML(DraggablePanel.this.getName(), panelResult, ratioW, ratioH, subW, subH);
				}
			});
		}

		private void updatePanelSize() {
			int newWidth = (int) (originalWidth * ratioW);
			int newHeight = (int) (originalHeight * ratioH);

			currentWidth = newWidth; // 현재 크기를 업데이트하기
			currentHeight = newHeight;

			this.setSize(newWidth, newHeight); // draggablePanel에 현재 크기를 적용하기
			resizeImage(newWidth, newHeight);
			label.setSize(new Dimension(newWidth, newHeight)); // 이미지에 현재 크기를 적용하기
			createHitBoxes(); // 크기 조절점도 다시 갱신하기

			this.revalidate();
			this.repaint();
		}

		private void createHitBoxes() { // 조절점을 추가하는 함수
			if (!isSelected) {
				return;
			}
			int w = currentWidth;
			int h = currentHeight;
			hitBoxes.clear();

			// 모서리에 조절점 추가하기
			hitBoxes.add(new Rectangle(0, 0, cornerSize, cornerSize)); // 좌상단
			hitBoxes.add(new Rectangle(w - cornerSize, 0, cornerSize, cornerSize)); // 우상단
			hitBoxes.add(new Rectangle(0, h - cornerSize, cornerSize, cornerSize)); // 좌하단
			hitBoxes.add(new Rectangle(w - cornerSize, h - cornerSize, cornerSize, cornerSize)); // 우하단
			// 가장자리 중앙에 조절점 추가하기
			hitBoxes.add(new Rectangle(w / 2 - cornerSize / 2, 0, cornerSize, cornerSize)); // 상단 중앙
			hitBoxes.add(new Rectangle(w / 2 - cornerSize / 2, h - cornerSize, cornerSize, cornerSize)); // 하단 중앙
			hitBoxes.add(new Rectangle(0, h / 2 - cornerSize / 2, cornerSize, cornerSize)); // 왼쪽 중앙
			hitBoxes.add(new Rectangle(w - cornerSize, h / 2 - cornerSize / 2, cornerSize, cornerSize)); // 오른쪽 중앙

			if (hitBoxPanel != null) { // hitBoxPanel의 크기와 위치를 업데이트
				hitBoxPanel.setHitBoxes(hitBoxes);
				hitBoxPanel.setSize(new Dimension(w, h));
				hitBoxPanel.revalidate();
				hitBoxPanel.repaint();
			}
		}

		@Override
		protected void paintComponent(Graphics g) { // repaint 요청받을 때마다 그리기 실행
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g; // graphics 확장 버전의 객체로 캐스팅
			if (isSelected) {
				g2d.setColor(Color.RED);
				for (Rectangle rect : hitBoxes) {
					g2d.fill(rect); // hitBoxes에 저장된 rect 객체들을 Color.red로 칠하기
				}
			}
		}

		public void resizeImage(int width, int height) { // 이미지 크기 조절할 때마다 해상도가 안 깨지도록 원본이미지를 기준으로 크기 조절하기
			Image resizedImg = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(resizedImg));
		}

		private void resizePanel(Point p) {
			int dx = p.x - initialClick.x;
			int dy = p.y - initialClick.y;
			int newWidth = getWidth();
			int newHeight = getHeight();

			// 선택된 조절점에 따라 패널 크기 조절 로직 구현하기
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
			} else if (hitBoxIndex == 4) { // 상단 중앙
				newHeight = getHeight() - dy;
				setBounds(getX(), getY() + dy, getWidth(), newHeight); // 변수별로 받아서 x,y
			} else if (hitBoxIndex == 5) { // 하단 중앙
				newHeight = getHeight() + dy;
				setBounds(getX(), getY(), getWidth(), newHeight);
			} else if (hitBoxIndex == 6) { // 왼쪽 중앙
				newWidth = getWidth() - dx;
				setBounds(getX() + dx, getY(), newWidth, getHeight());
			} else if (hitBoxIndex == 7) { // 오른쪽 중앙
				newWidth = getWidth() + dx;
				setBounds(getX(), getY(), newWidth, getHeight());
			}

			setBounds(getX(), getY(), newWidth, newHeight);
			label.setSize(newWidth, newHeight);
			resizeImage(newWidth, newHeight);
			currentWidth = newWidth;
			currentHeight = newHeight;

			// 변경 전 크기 대비 현재 크기의 비율 구하기
			ratioW = currentWidth / originalWidth;
			ratioH = currentHeight / originalHeight;
			createHitBoxes();
			hitBoxPanel.setBounds(0, 0, currentWidth, currentHeight);

			dropSendSizeChangeXML(this.getName(), panelResult, ratioW, ratioH, 0, 0); // 함수 호출해서 통합 컨트롤러에서 변경할 때마다 하이퍼월
																						// 컨트롤러도 변경
			revalidate();
		}

		// 컴포넌트의 위치나 크기가 변경되었을 경우 하이퍼월 컨트롤러에 반영하기 위한 XML 명령 보내는 함수
		private void dropSendSizeChangeXML(String id, int panelNumber, float currentWidthRatio,
				float currentHeightRatio, int subW, int subH) {
			try {

				int[] pointArray = getXYPoint(panelNumber); // 패널 번호에 따른 하이퍼월 컨트롤러의 좌표를 계산하기

				String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"; // xmlString 변수에다
																									// 변경값을 보낼 XML 명령 작성
				xmlString += "<Commands>";
				xmlString += "<command type=\"change\">";
				xmlString += "<id>" + id + "</id>";

				// 패널 번호에 따른 변경된 위치를 하이퍼월 컨트롤러의 x,y좌표로 보내기
				if (panelNumber == 1) {
					System.out.println(currentWidthRatio);
					xmlString += "<x>" + (pointArray[0] + (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 2) {
					xmlString += "<x>" + (pointArray[0] * (2 - currentWidthRatio)) + "</x>";
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 3) {
					xmlString += "<x>" + (pointArray[0] * currentWidthRatio) + "</x>";
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 4) {
					xmlString += "<x>" + (pointArray[0] - (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					System.out.println(xmlString);
					xmlString += "<y>" + (2 * pointArray[1] - (pointArray[1] * currentHeightRatio)) + "</y>";
				}
				if (panelNumber == 5) {
					xmlString += "<x>" + (pointArray[0] + (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					xmlString += "<y>" + ((pointArray[1] * currentHeightRatio)) + "</y>";
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
				if (panelNumber == 8) {
					xmlString += "<x>" + (pointArray[0] - (pointArray[0] * (currentWidthRatio - 1) * -1 / 3)) + "</x>";
					xmlString += "<y>" + ((pointArray[1] * currentHeightRatio)) + "</y>";
				}

				xmlString += "<boundsfill>1</boundsfill>";
				xmlString += "<boundsh>" + (1080 * currentHeightRatio) + "</boundsh>"; // ratio값을 이용해 변경된 너비, 높이값을 하이퍼월
																						// 컨트롤러로 보내기
				xmlString += "<boundsw>" + (1927 * currentWidthRatio) + "</boundsw>";
				xmlString += "</command>";
				xmlString += "</Commands>";

				URL requestURL = new URL(baseURL + "/xmlcommand");
				HttpURLConnection requestConnection = (HttpURLConnection) requestURL.openConnection();
				requestConnection.setRequestMethod("POST");
				requestConnection.setDoOutput(true);
				OutputStream reqStream = requestConnection.getOutputStream();
				reqStream.write(xmlString.getBytes("UTF8"));

				if (requestConnection.getResponseCode() == 200) { // http 요청이 성공적으로 수행된 경우
					for (openFileList item : openFileList) {
						if (item.id.equals(id)) {
							item.panelNumber = panelNumber; // 모든 openFileList를 순회했을 때 오픈된 파일의 id가 item의 id가 일치하는 컨텐츠가
															// 새로운 패널로 옮겨짐
						}
					}

					SwingUtilities.invokeLater(new Runnable() { // Swing 컴포넌트의 상태를 변경하는 경우 SwingUtilities.invokeLater 사용
						public void run() {

							DraggablePanel selectedPanel = null;
							for (DraggablePanel panel : dragPanelOpenList) {
								if (panel.getName().equals(id)) { // 모든 dragPanelOpenList를 순회했을 때 해당 리스트와 id가 일치하는 패널을
																	// 찾는 경우
									selectedPanel = panel; // selectedPanel을 panel로 갱신
								}
							}

							int uiPointArray[] = getUiPoint(panelNumber); // panelNumber에 해당하는 통합 컨트롤러의 UI 좌표 계산하기

							selectedPanel.setBounds(uiPointArray[0], uiPointArray[1],
									(int) (selectedPanel.getPreferredSize().width * currentWidthRatio),
									(int) (selectedPanel.getPreferredSize().height * currentHeightRatio)); // 선택된 패널의
																											// 위치와 크기
																											// 설정하기
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
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다.\n" + e, "Warning", JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다.\n" + e, "Warning",
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
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다.\n" + e, "Warning", JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다.\n" + e, "Warning",
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
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다.\n" + e, "Warning", JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다.\n" + e, "Warning",
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
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다.\n" + e, "Warning", JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다.\n" + e, "Warning",
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
				JOptionPane.showMessageDialog(frame, "연결을 거부했습니다.\n" + e, "Warning", JOptionPane.INFORMATION_MESSAGE);
			} else if (e != null && e.toString().contains("Connection timed out")) {
				JOptionPane.showMessageDialog(frame, "연결 시간이 초과되었습니다.\n" + e, "Warning",
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

	// CONTENTS 파일에서 폴더와 파일을 Tree 형태로 생성
	public DefaultMutableTreeNode getFile(String path) {
		File file = new File(path);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName());

		if (file.isDirectory()) {
			// file 경로 안 파일들을 불러옴
			File[] files = file.listFiles();

			if (files.length == 0) {
				root.add(new DefaultMutableTreeNode(file.getName()));
			}
			if (files != null) {
				for (File child : files) {

					// 특정 확장자를 제외하고 root 트리에 추가
					if (!child.toString().endsWith(".hwc") && !child.toString().endsWith(".hws")
							&& !child.toString().endsWith(".hwtxt") && !child.toString().contains("HiperwallAnimations")
							&& !child.toString().contains("hiperwall_thumbs")
							&& !child.toString().contains("schedules")) {
						root.add(getFile(child.getAbsolutePath()));

						// 만약 .이 포함된 파일이라면 controllerFileList에 추가
						if (child.toString().contains(".")) {
							controllerFileList.add(child.toString());

							// 만약 .이 포함되지 않고 .으로 끝나지 않는 폴더라면 folderNameList에 추가
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

	// 통합 컨트롤러 시작점
	public static void main(String[] args) {
		new Controller_main();
	}

}

// 열려있는 파일을 담는 배열을 만들기 위한 클래스 
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