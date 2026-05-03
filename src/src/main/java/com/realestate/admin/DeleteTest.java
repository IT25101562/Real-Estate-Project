package com.realestate.admin;
import com.realestate.admin.dao.AdminDAO;

public class DeleteTest {
    public static void main(String[] args) {
        AdminDAO dao = new AdminDAO();
        boolean success = dao.deleteAdmin(6);
        System.out.println("Delete success: " + success);
    }
}
