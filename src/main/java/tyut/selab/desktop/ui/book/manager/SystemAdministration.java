package tyut.selab.desktop.ui.book.manager;
import tyut.selab.desktop.moudle.book.bookcontroller.impl.BookBorrowController;
import tyut.selab.desktop.moudle.book.bookcontroller.impl.BookMessageController;
import tyut.selab.desktop.moudle.book.domain.Book;
import tyut.selab.desktop.moudle.book.domain.vo.BookVo;
import tyut.selab.desktop.moudle.student.domain.vo.UserVo;
import tyut.selab.desktop.moudle.student.usercontroller.impl.UserController;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SystemAdministration extends JPanel {

    // 书籍价格升序
    public static final int BOOK_PRICE_ASC = 0;
    // 按书籍价格降序
    public static final int BOOK_PRICE_DESC = 1;
    // 查找已借出书籍
    public static final int BOOK_LENDED = 2;
    // 查找未借出书籍
    public static final int BOOK_LENDABLE = 3;
    BookMessageController bookMessageController = new BookMessageController();

    BookBorrowController bookBorrowController = new BookBorrowController();

    // 用于展示的书籍数据和表头  (用 Vector 代替数组)
    private Vector<String> titlesV = null;
    private Vector<Vector> dataV = null;

    UserController userController = new UserController();
   private  DefaultTableModel defaultTableModel = null;
   private JTable jt = null;
    public SystemAdministration() throws SQLException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        titlesV = getTitleVector();
        dataV = getDataVector(bookMessageController.queryAllBook());

        setLayout(null);

        // 建两个panel 一个固定菜单按钮不进行刷新 一个面板用于展示刷新
        // 用于刷新变更的面板 放jtable
        JPanel viewJPanel = new JPanel();
        viewJPanel.setBounds(250, 130, 1010, 600);
        viewJPanel.setBackground(Color.cyan);


        //jtable的创建
         defaultTableModel = new DefaultTableModel(dataV,titlesV){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jt = new JTable(defaultTableModel);
        jt.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn column = null;
        //设置列宽
        int colunms = jt.getColumnCount();
        int[] columnList = {350, 300, 400, 300, 310, 350, 400, 400};
        for (int i = 0; i < titlesV.size(); i++) {
            column = jt.getColumnModel().getColumn(i);
            /*将每一列的默认宽度设置为100*/
            column.setPreferredWidth(columnList[i]);
        }


        JScrollPane js = new JScrollPane(jt) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(750, 450);
            }
        };
        js.setBounds(0,0,1010,600);
        viewJPanel.setBounds(250, 130, 1010, 600);
        viewJPanel.add(js);
        viewJPanel.setVisible(true);
        add(viewJPanel);

        // 菜单面板
        JPanel menuJPanel = new JPanel();
        menuJPanel.setBounds(250, 30, 1010, 100);
        menuJPanel.setVisible(true);
        menuJPanel.setBackground(Color.PINK);
        menuJPanel.setLayout(null);
        add(menuJPanel);

        // 菜单上的按钮
        // 增加书籍
        JButton addButton = new JButton("增加书籍");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               MyDialogInsert myDialogInsert = new MyDialogInsert();
                myDialogInsert.setVisible(true);
            }
        });
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 15));
        addButton.setBackground(new Color(0xFFFF4E4E, true));
        addButton.setForeground(Color.white);
        addButton.setBounds(160, 10, 150, 40);
        addButton.setFocusPainted(false);                         // 按钮字周围的小光圈
        menuJPanel.add(addButton);

        // 修改书籍

        JButton changeButton = new JButton("修改书籍");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int updateSelectedRow = jt.getSelectedRow();
                MyJDialogUpdate myJDialog = new MyJDialogUpdate(updateSelectedRow);
                myJDialog.setVisible(true);
            }
        });
        changeButton.setFont(new Font("微软雅黑", Font.BOLD, 15));            //根据主界面写的
        changeButton.setBackground(new Color(0xFFFF4E4E, true));
        changeButton.setForeground(Color.white);
        changeButton.setBounds(340, 10, 150, 40);
        changeButton.setFocusPainted(false);                              // ？
        menuJPanel.add(changeButton);

        //删除书籍
        JButton deleteButton = new JButton("删除书籍");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jt.getSelectedRow();
                MyJDialogDelete myJDialogDelete = new MyJDialogDelete(selectedRow);
                myJDialogDelete.setVisible(true);
            }
        });
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 15));              //根据主界面写的
        deleteButton.setBackground(new Color(0xFFFF4E4E, true));
        deleteButton.setForeground(Color.white);
        deleteButton.setBounds(520, 10, 150, 40);
        deleteButton.setFocusPainted(false);
        menuJPanel.add(deleteButton);

        //黑名单
        JButton blacklistButton = new JButton("黑名单");
        blacklistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> titleBlackList = getTitleBlackList();

                Vector<Vector> valuesBlackList = null;
                try {
                    valuesBlackList = queryBlackList();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
                MyJDialogBlackList myJDialogBlackList = new MyJDialogBlackList(titleBlackList, valuesBlackList);
                myJDialogBlackList.setVisible(true);
            }
        });
        blacklistButton.setFont(new Font("微软雅黑", Font.BOLD, 15));          //根据主界面写的
        blacklistButton.setBackground(new Color(0xFFFF4E4E, true));
        blacklistButton.setForeground(Color.white);
        blacklistButton.setBounds(700, 10, 150, 40);
        blacklistButton.setFocusPainted(false);
        menuJPanel.add(blacklistButton);

        //下拉选择
        JComboBox<String> jComboBox = new JComboBox<String>();
        jComboBox.addItem("-按价格升序排列-");
        jComboBox.addItem("-按价格降序排列-");
        jComboBox.addItem("-查看未借出书籍-");
        jComboBox.addItem("-查看未归还书籍-");
        jComboBox.addItem("-查看全部书籍-");
        jComboBox.setBounds(20,60,120,30);
        jComboBox.setBackground(Color.PINK);
        jComboBox.setForeground(Color.black);
        jComboBox.setFont( new Font("微软雅黑", Font.BOLD, 10));
        String chooseItem = jComboBox.getSelectedItem().toString();
        List<BookVo> bookVos = bookMessageController.queryAllBook();
        List<BookVo> bookVos1 = null;
        switch(chooseItem){
            case "-按价格升序排列-":
                bookVos1 = sortBookVos(bookVos, BOOK_PRICE_ASC);
                break;
            case "-按价格降序排列-":
                bookVos1 = sortBookVos(bookVos,BOOK_PRICE_DESC);
                break;
            case "-查看未借出书籍-":
                for (int i = 0; i < bookVos.size(); i++) {
                     if(bookVos.get(i).getBookStatus() == Book.LENDABLE){
                         bookVos1.add(bookVos.get(i));
                     }
                }
                break;
            case "-查看未归还书籍-":
                bookVos1 = bookMessageController.queryBorrowBookLog();
                break;
            case "-查看全部书籍-":
                bookVos1 = bookVos;
                break;
            default:
                break;
        }
           dataV = getDataVector(bookVos1);
        defaultTableModel.setDataVector(dataV,titlesV);
        menuJPanel.add(jComboBox);

        //文字标签1
        JLabel jlName = new JLabel("书籍名称:",JLabel.CENTER);
        jlName.setFont( new Font("微软雅黑", Font.BOLD, 13) );
        jlName.setBounds(160,60,60,30);
        jlName.setForeground(Color.WHITE);
        menuJPanel.add(jlName);

        //文本框1
        JTextField jtName = new JTextField();
        jtName.setBounds(230,60,250,30);
        menuJPanel.add(jtName);

        //文字标签2
        JLabel jlStudentId = new JLabel("学生学号:",JLabel.CENTER);
        jlStudentId.setFont( new Font("微软雅黑", Font.BOLD, 13) );
        jlStudentId.setForeground(Color.WHITE);
        jlStudentId.setBounds(500,60,60,30);
        menuJPanel.add(jlStudentId);

        //文本框2
        JTextField jtStudentId = new JTextField();
        jtStudentId.setBounds(570,60,250,30);
        menuJPanel.add(jtStudentId);

        // 查询书籍
        // 之后要插入图片
        JButton searchButton = new JButton();
        searchButton.setBounds(855,60,30, 30);
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "C:\\Users\\26580\\Desktop\\desket_platfrom-main\\ui_text\\R-C.png");
        searchButton.setIcon(icon);
        searchButton.setOpaque(false);                       //设置控件是否透明，true为不透明，false为透明
        searchButton.setContentAreaFilled(false);               //设置图片填满按钮所在的区域
        searchButton.setBorderPainted(false);                //设置是否绘制边框
        searchButton.setBorder(null);                      //设置边框
        menuJPanel.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textName = jtName.getText();
                String textStudentId = jtStudentId.getText();
                if (textName != "") {
                    if (textStudentId != "") {
                        BookVo bookVo = null;
                        try {
                            bookVo = bookMessageController.queryBorrowBookLog(textName, Integer.valueOf(textStudentId));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (NoSuchFieldException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (InstantiationException ex) {
                            throw new RuntimeException(ex);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                        List<BookVo> bookVos = new ArrayList<>();
                        bookVos.add(bookVo);
                        dataV = getDataVector(bookVos);
                        defaultTableModel.setDataVector(dataV, titlesV);
                    } else {
                        List<BookVo> bookVos = null;
                        try {
                            bookVos = bookMessageController.queryBorrowBookLog(textName);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (NoSuchFieldException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (InstantiationException ex) {
                            throw new RuntimeException(ex);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                        dataV = getDataVector(bookVos);
                        defaultTableModel.setDataVector(dataV, titlesV);
                    }
                } else {
                    if (textStudentId != "") {
                        List<BookVo> bookVos = null;
                        try {
                            bookVos = bookMessageController.queryBorrowBookLog(textStudentId);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (NoSuchFieldException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (InstantiationException ex) {
                            throw new RuntimeException(ex);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                        dataV = getDataVector(bookVos);
                        defaultTableModel.setDataVector(dataV, titlesV);
                    } else {
                        List<BookVo> bookVos = null;
                        try {
                            bookVos = bookMessageController.queryAllBook();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (NoSuchFieldException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (InstantiationException ex) {
                            throw new RuntimeException(ex);
                        } catch (IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                        dataV = getDataVector(bookVos);
                        defaultTableModel.setDataVector(dataV, titlesV);
                    }
                }
            }
        });
        }



    //    点击黑名单时弹出页面
    private class MyJDialogBlackList extends JDialog {
        Vector<String> titleBlackList = null;
        Vector<Vector> valuesList = null;

        public MyJDialogBlackList(Vector<String> titleBlackList, Vector<Vector> valuesList) {
            this.titleBlackList = titleBlackList;
            this.valuesList = valuesList;
            setTitle("黑名单列表");
            setModal(true);
            setSize(800, 500);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            DefaultTableModel defaultTableModel1 = new DefaultTableModel(valuesList,titleBlackList);
            JTable jTable = new JTable(defaultTableModel1);
            JScrollPane jScrollPane = new JScrollPane(jTable);
            add(jScrollPane);

        }
    }

    //   删除书籍时弹出的对话框
        //TODO:   样式

        private class MyJDialogDelete extends JDialog {

            public MyJDialogDelete(int updateSelectColmn) {
                setTitle("删除书籍");
                setModal(true);
                setSize(800, 500);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setLocationRelativeTo(null);
                JLabel jlName = new JLabel("书籍名称");
                jlName.setFont(new Font("微软黑体",Font.BOLD,15));
                jlName.setBounds(220,25,100,30);
                JLabel jlNameContent = new JLabel((String) dataV.get(updateSelectColmn).get(0));
                jlNameContent.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlNameContent.setBounds(330,25,300,30);
                JLabel jlOwner = new JLabel("书籍拥有者");
                jlOwner.setFont(new Font("微软黑体",Font.BOLD,15));
                jlOwner.setBounds(220,65,100,30);
                JLabel jlOwnerConter = new JLabel((String) dataV.get(updateSelectColmn).get(1));
                jlOwnerConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlOwnerConter.setBounds(330,65,300,30);
                JLabel jlOwnerStudentId = new JLabel("拥有者学号");
                jlOwnerStudentId.setFont(new Font("微软黑体",Font.BOLD,15));
                jlOwnerStudentId.setBounds(220,105,100,30);
                JLabel jlOwnerStudentIdConter = new JLabel(dataV.get(updateSelectColmn).get(2).toString());
                jlOwnerStudentIdConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlOwnerStudentIdConter.setBounds(330,105,300,30);
                JLabel jlPrice = new JLabel("书籍价格");
                jlPrice.setFont(new Font("微软黑体",Font.BOLD,15));
                jlPrice.setBounds(220,155,100,30);
                JLabel jlPriceConter = new JLabel(dataV.get(updateSelectColmn).get(3).toString());
                jlPriceConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlPriceConter.setBounds(330,155,300,30);
                jlPriceConter.setSize(100, 30);
                JLabel jlStatus = new JLabel("书籍状态");
                jlStatus.setFont(new Font("微软黑体",Font.BOLD,15));
                jlStatus.setBounds(220,195,100,30);
                JLabel jlStatusConter = new JLabel(dataV.get(updateSelectColmn).get(5).toString());
                jlStatusConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlStatusConter.setBounds(330,195,300,30);
                JLabel jlBorrower = new JLabel("借阅人");
                jlBorrower.setFont(new Font("微软黑体",Font.BOLD,15));
                jlBorrower.setBounds(220,235,100,30);
                JLabel jlBorrowerConter = new JLabel((String) dataV.get(updateSelectColmn).get(4));
                jlBorrowerConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlBorrowerConter.setBounds(330,235,300,30);
                JLabel jlBorrowTime = new JLabel("借出时间");
                jlBorrowTime.setFont(new Font("微软黑体",Font.BOLD,15));
                jlBorrowTime.setBounds(220,275,100,30);
                JLabel jlBorrowTimeConter = new JLabel(dataV.get(updateSelectColmn).get(6).toString());
                jlBorrowTimeConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlBorrowTimeConter.setBounds(330,275,300,30);
                JLabel jlReturnTime = new JLabel("还书时间");
                jlReturnTime.setFont(new Font("微软黑体",Font.BOLD,15));
                jlReturnTime.setBounds(220,315,100,30);
                JLabel jlReturnTimeConter = new JLabel(dataV.get(updateSelectColmn).get(7).toString());
                jlReturnTimeConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                jlReturnTimeConter.setBounds(330,315,300,30);
                JPanel jp = new JPanel();
                jp.setSize(800,500);
                jp.setBackground(Color.WHITE);
                jp.setLayout(null);
                JButton jb = new JButton("确认删除");
                jb.setFont(new Font("微软黑体",Font.BOLD,15));
                jb.setBounds(250,380,300,40);
                jb.setBackground(Color.PINK);
                jb.setForeground(Color.WHITE);
                jb.setFocusPainted(false);
                jp.add(jlName);
                jp.add(jlNameContent);
                jp.add(jlOwner);
                jp.add(jlOwnerConter);
                jp.add(jlOwnerStudentId);
                jp.add(jlOwnerStudentIdConter);
                jp.add(jlPrice);
                jp.add(jlPriceConter);
                jp.add(jlStatus);
                jp.add(jlStatusConter);
                jp.add(jlBorrower);
                jp.add(jlBorrowerConter);
                jp.add(jlBorrowTime);
                jp.add(jlBorrowTimeConter);
                jp.add(jlReturnTime);
                jp.add(jlReturnTimeConter);
                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirmDialog = JOptionPane.showConfirmDialog(null, "请确定是否删除此书籍", "确认删除", JOptionPane.YES_NO_OPTION);
                        if(confirmDialog == JOptionPane.YES_OPTION){
                            int selectedRow = jt.getSelectedRow();
                            dataV.remove(selectedRow);
                            defaultTableModel.setDataVector(dataV,titlesV);
                            try {
                                int flag = bookMessageController.deleteBook(Integer.valueOf(jlOwnerStudentIdConter.getText()), jlNameContent.getText());
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            setVisible(false);
                        }else{
                            setVisible(false);
                        }

                    }
                });
                jp.add(jb);
                add(jp);
            }
        }

        //增加书籍的弹出对话框
        private class MyDialogInsert extends JDialog {
            public MyDialogInsert() {
                ImageIcon imageIcon = new ImageIcon(System.getProperty("user.dir") + "\\src\\ManagerGui\\ddot.png");
                setIconImage(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

                setTitle("增加书籍");
                setModal(true);
                setSize(800, 500);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setLocationRelativeTo(null);
                JLabel jlName = new JLabel("书籍名称:",JLabel.CENTER);
                jlName.setFont(new Font("微软黑体",Font.BOLD,20));
                jlName.setBounds(50,10,200,80);
                JTextField jlNameContent = new JTextField();
                jlNameContent.setBounds(260,10,400,80);
                jlNameContent.setFont(new Font("微软黑体",Font.PLAIN,20));
                jlNameContent.setHorizontalAlignment(JTextField.CENTER);
                JLabel jlOwner = new JLabel("书籍拥有者:",JLabel.CENTER);
                jlOwner.setFont(new Font("微软黑体",Font.BOLD,20));
                jlOwner.setBounds(50,100,200,80);
                JTextField jlOwnerConter = new JTextField();
                jlOwnerConter.setBounds(260,100,400,80);
                jlOwnerConter.setFont(new Font("微软黑体",Font.PLAIN,20));
                jlOwnerConter.setHorizontalAlignment(JTextField.CENTER);

                JLabel jlOwnerStudnetId = new JLabel("拥有者学号:",JLabel.CENTER);
                jlOwnerStudnetId.setFont(new Font("微软黑体",Font.BOLD,20));
                jlOwnerStudnetId.setBounds(50,190,200,80);
                JTextField jlOwnerStudentIdConter = new JTextField();
                jlOwnerStudentIdConter.setBounds(260,190,400,80);
                jlOwnerStudentIdConter.setFont(new Font("微软黑体",Font.PLAIN,20));
                jlOwnerStudentIdConter.setHorizontalAlignment(JTextField.CENTER);

                JLabel jlPrice = new JLabel("书籍价格:",JLabel.CENTER);
                jlPrice.setFont(new Font("微软黑体",Font.BOLD,20));
                jlPrice.setBounds(50,280,200,80);
                JTextField jlPriceConter = new JTextField();
                jlPriceConter.setBounds(260,280,400,80);
                jlPriceConter.setFont(new Font("微软黑体",Font.PLAIN,20));
                jlPriceConter.setHorizontalAlignment(JTextField.CENTER);

                JButton jb = new JButton("确定添加");
                jb.setBounds(150,380,500,60);
                jb.setFont(new Font("微软雅黑", Font.BOLD, 15));
                jb.setBackground(Color.PINK);
                jb.setForeground(Color.WHITE);
                jb.setFocusPainted(false);
                JPanel jp = new JPanel();
                jp.setBounds(0,0,800,500);
                jp.setBackground(Color.WHITE);
                jp.setLayout(null);
                add(jp);
                jp.add(jlName);
                jp.add(jlNameContent);
                jp.add(jlOwner);
                jp.add(jlOwnerConter);
                jp.add(jlOwnerStudnetId);
                jp.add(jlOwnerStudentIdConter);
                jp.add(jlPrice);
                jp.add(jlPriceConter);
                jp.add(jb);
                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jlNameContent.getText().trim().equals("") || jlOwnerStudentIdConter.getText().trim().equals("") || jlOwnerConter.getText().trim().equals("") || jlPriceConter.getText().trim().equals("")) {
                            JOptionPane jOptionPane = new JOptionPane();
                            JOptionPane.showMessageDialog(null, "书籍信息不能为空", "警告框", JOptionPane.WARNING_MESSAGE);
                        } else {
                            Vector vector = new Vector();
                            vector.add(jlNameContent.getText().trim());
                            vector.add(jlOwnerConter.getText().trim());
                            vector.add(jlOwnerStudentIdConter.getText().trim());
                            vector.add(jlPrice.getText().trim());
                            vector.add("未借出");
                            vector.add("");
                            vector.add("");
                            vector.add("");
                            dataV.add(vector);
                            BookVo bookVo = new BookVo();
                            bookVo.setBookName(jlNameContent.getText());
                            bookVo.setBookPrice(Double.valueOf(jlPriceConter.getText()));
                            UserVo userVo = userController.queryUserByStudentNumber(Integer.valueOf(jlOwnerStudentIdConter.getText()));
                            bookVo.setBookUserVo(userVo);
                            bookVo.setBookStatus(Book.LENDABLE);
                            try {
                                System.out.println(bookMessageController + "=======");
                                bookMessageController.insertBook(bookVo);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            defaultTableModel.setDataVector(dataV, titlesV);
                            setVisible(false);
                        }
                    }
                });
            }
        }

        //  修改书籍信息是弹出的对话框

        private class MyJDialogUpdate extends JDialog {
            public MyJDialogUpdate(int updateSelectColmn) {
                ImageIcon imageIcon = new ImageIcon(System.getProperty("user.dir") + "\\src\\ManagerGui\\ddot.png");
                setIconImage(imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                setTitle("修改书籍信息");
                setModal(true);
                setSize(800, 500);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);    // 关闭后销毁对话框
                setLocationRelativeTo(null);
                JLabel jlName = new JLabel("书籍名称:",JLabel.CENTER);
                jlName.setBounds(190,25,100,30);
                jlName.setFont(new Font("微软黑体",Font.BOLD,15));
                JLabel jlNameContent = new JLabel((String) dataV.get(updateSelectColmn).get(0),JLabel.CENTER);
                jlNameContent.setBounds(300,25,300,30);
                jlNameContent.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlOwner = new JLabel("书籍拥有者:",JLabel.CENTER);
                jlOwner.setBounds(190,65,100,30);
                jlOwner.setFont((new Font("微软黑体",Font.BOLD,15)));
                JLabel jlOwnerConter = new JLabel((String) dataV.get(updateSelectColmn).get(1),JLabel.CENTER);
                jlOwnerConter.setBounds(300,65,300,30);
                jlOwnerConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlOwnerStudentid = new JLabel("拥有者学号:",JLabel.CENTER);
                jlOwnerStudentid.setBounds(190,105,100,30);
                jlOwnerStudentid.setFont(new Font("微软黑体",Font.BOLD,15));
                JLabel jlOwnerStudentIdConter = new JLabel( dataV.get(updateSelectColmn).get(2).toString(),JLabel.CENTER);
                jlOwnerStudentIdConter.setBounds(300,105,300,30);
                jlOwnerStudentIdConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlPrice = new JLabel("书籍价格:",JLabel.CENTER);
                jlPrice.setBounds(190,155,100,30);
                jlPrice.setFont(new Font("微软黑体",Font.BOLD,15));
                JTextField jlPriceConter = new JTextField( dataV.get(updateSelectColmn).get(3).toString());
                jlPriceConter.setHorizontalAlignment(JTextField.CENTER);
                jlPriceConter.setBounds(300,155,300,30);
                jlPriceConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlStatus = new JLabel("书籍状态:",JLabel.CENTER);
                jlStatus.setBounds(190,195,100,30);
                jlStatus.setFont(new Font("微软黑体",Font.BOLD,15));
                JComboBox jComboBox =new JComboBox<>();
                jComboBox.addItem("                               "+dataV.get(updateSelectColmn).get(4).toString());
                if(dataV.get(updateSelectColmn).equals(Book.LENDABLE)){
                    jComboBox.addItem("                               "+"未借出");
                }else{
                    jComboBox.addItem("                               "+"已借出");      //9个tab-2空格
                }
                jComboBox.setBounds(300,195,300,30);
                jComboBox.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlBorrower = new JLabel("借阅人:",JLabel.CENTER);
                jlBorrower.setBounds(190,235,100,30);
                jlBorrower.setFont(new Font("微软黑体",Font.BOLD,15));
                JTextField jlBorrowerConter = new JTextField((String) dataV.get(updateSelectColmn).get(5));
                jlBorrowerConter.setHorizontalAlignment(JTextField.CENTER);
                jlBorrowerConter.setBounds(300,235,300,30);
                jlBorrowerConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlBorrowerStudentId = new JLabel("借阅者学号:",JLabel.CENTER);
                jlBorrowerStudentId.setBounds(190,235,100,30);
                jlBorrowerStudentId.setFont(new Font("微软黑体",Font.BOLD,15));
                JTextField jlBorrowerStudentIdConter = new JTextField((String) dataV.get(updateSelectColmn).get(5));
                jlBorrowerStudentIdConter.setHorizontalAlignment(JTextField.CENTER);
                jlBorrowerStudentIdConter.setBounds(300,235,300,30);
                jlBorrowerStudentIdConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlBorrowTime = new JLabel("借出时间:",JLabel.CENTER);
                jlBorrowTime.setBounds(190,275,100,30);
                jlBorrowTime.setFont(new Font("微软黑体",Font.BOLD,15));
                JLabel jlBorrowTimeConter = new JLabel(dataV.get(updateSelectColmn).get(6).toString(),JLabel.CENTER);
                jlBorrowTimeConter.setBounds(300,275,300,30);
                jlBorrowTimeConter.setFont(new Font("微软黑体",Font.PLAIN,15));

                JLabel jlReturnTime = new JLabel("还书时间:",JLabel.CENTER);
                jlReturnTime.setBounds(190,315,100,30);
                jlReturnTime.setFont(new Font("微软黑体",Font.BOLD,15));
                JTextField jlReturnTimeConter = new JTextField(dataV.get(updateSelectColmn).get(7).toString()+"(请使用yyyy-MM-dd HH:mm:ss 格式)");
                jlReturnTimeConter.setHorizontalAlignment(JTextField.CENTER);
                jlReturnTimeConter.setBounds(300,315,300,30);
                jlReturnTimeConter.setFont(new Font("微软黑体",Font.PLAIN,15));
                JButton jb = new JButton("确认修改");
                jb.setFont(new Font("微软黑体",Font.BOLD,15));
                jb.setBounds(250,380,300,40);
                jb.setBackground(Color.PINK);
                jb.setForeground(Color.WHITE);
                jb.setFocusPainted(false);

                JPanel jp = new JPanel();
                jp.setBounds(0,0,800,500);
                jp.setBackground(Color.WHITE);
                jp.setLayout(null);
                add(jp);
                jp.add(jb);
                jp.add(jlName);
                jp.add(jlNameContent);
                jp.add(jlOwner);
                jp.add(jlOwnerConter);
                jp.add(jlOwnerStudentid);
                jp.add(jlOwnerStudentIdConter);
                jp.add(jlPrice);
                jp.add(jlPriceConter);
                jp.add(jlStatus);
                jp.add(jComboBox);
                jp.add(jlBorrower);
                jp.add(jlBorrowerConter);
                jp.add(jlBorrowTime);
                jp.add(jlBorrowTimeConter);
                jp.add(jlReturnTime);
                jp.add(jlReturnTimeConter);

                jb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jlOwnerStudentIdConter.getText().trim().equals("") || jlOwnerConter.getText().trim().equals("") || jlPriceConter.getText().trim().equals("")) {
                            JOptionPane.showMessageDialog(null, "书籍信息不能为空", "警告框", JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (jComboBox.getSelectedItem().toString().trim().equals("已借出") && (jlBorrowerConter.getText().trim().equals("") || jlReturnTimeConter.getText().trim().equals(""))) {
                                JOptionPane.showMessageDialog(null, "请输入借阅者信息或归还时间", "输入错误", JOptionPane.WARNING_MESSAGE);
                            } else {

                                int confirmDialog = JOptionPane.showConfirmDialog(null, "请确定是否修改此书籍", "确认修改", JOptionPane.YES_NO_OPTION);
                                if (confirmDialog == JOptionPane.YES_OPTION) {
                                    int selectedRow = jt.getSelectedRow();
                                    String bookName = dataV.get(selectedRow).get(0).toString();
                                    Object studentId = dataV.get(selectedRow).get(2);
                                    dataV.get(selectedRow).set(3, jlPriceConter.getText().trim());
                                    dataV.get(selectedRow).set(4, jComboBox.getSelectedItem().toString().trim());
                                    if (jComboBox.getSelectedItem().toString().trim().equals("已借出")) {
                                        dataV.get(selectedRow).set(5, jlBorrowTimeConter.getText().trim());
                                        dataV.get(selectedRow).set(7, jlReturnTimeConter.getText().trim());
                                    } else {
                                        dataV.get(selectedRow).set(5, "");
                                        dataV.get(selectedRow).set(7, "");
                                    }
                                    BookVo bookVo = null;
                                    try {
                                        bookVo = bookMessageController.queryBorrowBookLog(bookName, Integer.valueOf(studentId.toString()));
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (NoSuchFieldException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (ClassNotFoundException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (InstantiationException ex) {
                                        throw new RuntimeException(ex);
                                    } catch (IllegalAccessException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    UserVo userVo = userController.queryUserByStudentNumber((Integer) studentId);
                                    bookVo.setBookUserVo(userVo);
                                    bookVo.setBookStatus(jComboBox.getSelectedItem().toString().trim().equals("已借出") ? Book.LENDED : Book.LENDABLE);
                                    bookVo.setBookPrice(Double.valueOf(jlPriceConter.getText()));
                                    if (bookVo.getBookStatus() == Book.LENDED) {
                                        UserVo userVo1 = userController.queryUserByStudentNumber(Integer.valueOf(jlBorrowerStudentIdConter.getText().trim()));
                                        bookVo.setBorrowBookUserVo(userVo1);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date returnDate = null;
                                        try {
                                            returnDate = simpleDateFormat.parse(jlReturnTimeConter.getText());
                                        } catch (ParseException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                        bookVo.setReturnBookTime(returnDate);
                                    }
                                    defaultTableModel.setDataVector(dataV, titlesV);
                                    try {
                                        bookMessageController.updateBook(bookVo);
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    setVisible(false);
                                } else {
                                    setVisible(false);
                                }
                            }
                        }
                    }

                });
            }

        }



    /**
     * 封装书籍数据信息
     * @return
     */
    private Vector<Vector> getDataVector(List<BookVo> bookVos)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:mm");
        Vector<Vector> dataV = new Vector<>();
        for (int i = 0; i < bookVos.size(); i++) {
            Vector vector = new Vector();
            vector.add(bookVos.get(i).getBookName());
            vector.add(bookVos.get(i).getBookUserVo().getName());
            vector.add(bookVos.get(i).getBookUserVo().getStudentNumber());
            vector.add(bookVos.get(i).getBookPrice());
            int bookStatus = bookVos.get(i).getBookStatus();
            if(bookStatus == Book.LENDABLE){
                vector.add("未借出");
            }else{
                 vector.add("已借出");
            }
            if(bookVos.get(i).getBookStatus() == Book.LENDED) {
                vector.add(bookVos.get(i).getBorrowBookUserVo().getName());
                vector.add(simpleDateFormat.format(bookVos.get(i).getBorrowBookTime()));
                vector.add(simpleDateFormat.format(bookVos.get(i).getReturnBookTime()));
            }else{
                vector.add("");
                vector.add("");
                vector.add("");
            }
            dataV.add(vector);
        }
        return dataV;
    }

    /**
     * 封装书籍表头信息
     * @return
     */
    private Vector<String> getTitleVector() {
        Vector<String> titlesV = new Vector<>();
        titlesV.add("书籍名称");
        titlesV.add("书籍拥有者");
        titlesV.add("拥有者学号");
        titlesV.add("书籍价格");
        titlesV.add("书籍状态");
        titlesV.add("借阅人");
        titlesV.add("借出时间");
        titlesV.add("归还时间");
        return titlesV;
    }

    /**
     * 黑名单信息表头封装
     */
    public Vector<String> getTitleBlackList(){
        Vector<String> titleBlackList = new Vector<>();
        titleBlackList.add("学生姓名");
        titleBlackList.add("学生学号");
        titleBlackList.add("借阅书籍");
        titleBlackList.add("借阅时间");
        titleBlackList.add("承诺还书时间");
        titleBlackList.add("逾期天数");

        return titleBlackList;
    }

    /**
     * 查询黑名单列表
     * return Vector<Vector>
     */
    public Vector<Vector> queryBlackList() throws SQLException, NoSuchFieldException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<BookVo> bookVos = bookMessageController.queryBorrowBookLog();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Vector<Vector> blackList = new Vector<>();
        for (int i = 0; i < bookVos.size(); i++) {
            if(new Date().after(bookVos.get(i).getReturnBookTime())){
                Vector vector = new Vector();
                vector.add(bookVos.get(i).getBookUserVo().getName());
                vector.add(bookVos.get(i).getBookUserVo().getStudentNumber());
                vector.add(bookVos.get(i).getBookName());
                vector.add(simpleDateFormat.format(bookVos.get(i).getBorrowBookTime()));
                vector.add(simpleDateFormat.format(bookVos.get(i).getReturnBookTime()));
                vector.add((System.currentTimeMillis() - bookVos.get(i).getReturnBookTime().getTime())/(1000*60*60*24));
                blackList.add(vector);
            }
        }

        return blackList;
    }
    /**
     * 按某种方式对List<BookVo>排序
     */
    public List<BookVo> sortBookVos(List<BookVo> bookVos,int types) {

        if (types == BOOK_PRICE_ASC) {
            bookVos.sort(new Comparator<BookVo>() {
                @Override
                public int compare(BookVo o1, BookVo o2) {
                    return (int) (o1.getBookPrice() - o2.getBookPrice());
                }
            });
            return bookVos;
        }
        if (types == BOOK_PRICE_DESC) {
            bookVos.sort(new Comparator<BookVo>() {
                @Override
                public int compare(BookVo o1, BookVo o2) {
                    return (int) (o2.getBookPrice() - o1.getBookPrice());
                }
            });
            return bookVos;
        }
        if (types == BOOK_LENDED) {
            List<BookVo> bookVos1 = new ArrayList<>();
            for (int i = 0; i < bookVos.size(); i++) {
                if(bookVos.get(i).getBookStatus() == Book.LENDED){
                    bookVos1.add(bookVos.get(i));
                }
            }
            return bookVos1;
        }
        if (types == BOOK_LENDABLE) {
            List<BookVo> bookVos1 = new ArrayList<>();
            for (int i = 0; i < bookVos.size(); i++) {
                if(bookVos.get(i).getBookStatus() == Book.LENDABLE){
                    bookVos1.add(bookVos.get(i));
                }
            }
            return bookVos1;
        }
        return null;
    }


}
