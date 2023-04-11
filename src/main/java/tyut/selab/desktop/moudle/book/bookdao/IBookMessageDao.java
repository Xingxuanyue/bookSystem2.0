package tyut.selab.desktop.moudle.book.bookdao;


import tyut.selab.desktop.moudle.book.domain.Book;
import tyut.selab.desktop.moudle.book.domain.BookBorrow;

import java.util.List;

public interface IBookMessageDao {
    /**
     * 通过Id查找书籍
     */
    Book queryBookById(int bookId);
    /**
     * 查询借书记录
     * @return
     */
    List<BookBorrow> queryBorrowBookLog();

    /**
     * 根据书名来查询借书记录
     * @param bookBorrow
     * @return
     */
    List<BookBorrow> queryBorrowBookByName(String bookName);

    /**
     * 根据书的拥有者学号来查询借书记录
     * @param studentNumber
     * @return
     */
    List<BookBorrow> queryBorrowBookByUserid(Integer studentNumber);

    /**
     * 查询全部图书
     * @return 图书列表
     */
    List<Book> queryAllBook();

    /**
     * 通过图书名字查询图书
     * @param bookName 书名
     * @return 图书列表
     */
    List<Book> queryBookByBookName(String bookName);

    /**
     * 通过用户id查询该用户的图书
     * @param userStudentNumber 用户学号
     * @return 图书列表
     */
    List<Book> queryBookByUserid(Integer userStudentNumber);
    
     /**
     * 通过书籍名称和用户ID查询指定书籍
     * @return
     */
    Book queryAsBook(Integer userStudentNumber,String bookName);

    /**
     * 增加图书
     * @param book 书
     * @return 成功返回1，失败返回-1
     */
    int insertBook(Book book);

    /**
     * 修改图书信息
     * @param book 更新
     * @return
     */
    int updateBook(Book book);

    /**
     * 通过书名，修改图书状态
     * @param status
     * @param book
     * @return 成功返回1，失败返回-1
     */
    int updateBookStatus(int status,Book book);

    /**
     * 删除图书
     * @param userStudentNumber 用户学号
     * @param bookName 书名
     * @return 成功返回1，失败返回-1
     */
    int deleteBook(Integer userStudentNumber,String bookName);
}
