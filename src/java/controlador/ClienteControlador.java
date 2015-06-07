/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import entidades.Cliente;
import entidades.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.ClienteModelo;
import modelo.UsuarioModelo;

/**
 *
 * @author carlos
 */
@WebServlet(name = "ClienteControlado", urlPatterns = {"/cliente"})
public class ClienteControlador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
        String acao = request.getParameter("action");
        if(acao.equals("lista")) {
            request.getRequestDispatcher("/cliente/list.jsp").forward(request, response);
        } else if(acao.equals("novo")) {
            request.getRequestDispatcher("/cliente/create.jsp").forward(request, response);
        } else if(acao.equals("atualizar")) {
            request.getRequestDispatcher("/cliente/update.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            ClienteModelo modelo = new ClienteModelo(HibernateUtil.getSessionFactory());
            Cliente cliente = modelo.procurarPorId(new Long(request.getParameter("id")));
            modelo.excluir(cliente);
            request.getRequestDispatcher("/cliente/list.jsp").forward(request, response);
        } catch(Exception ex) {
            response.sendError(500);
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            ClienteModelo modelo = new ClienteModelo(HibernateUtil.getSessionFactory());
            if(request.getParameter("id").isEmpty()) { //Novo registro
                Cliente cliente = new Cliente();
                cliente.setId(new Random(1000).nextInt());
                cliente.setNome(request.getParameter("nome"));
                cliente.setCpf(request.getParameter("cpf"));
                cliente.setEndereco(request.getParameter("endereco"));
                cliente.setDataAniversario(new Date(request.getParameter("aniversario")));

                Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
                cliente.setUsuario(usuario);
                
                modelo.salvar(cliente);
            } else {
                
                Cliente cliente =  modelo.procurarPorId(new Long(request.getParameter("id")));
                cliente.setNome(request.getParameter("nome"));
                cliente.setCpf(request.getParameter("cpf"));
                cliente.setEndereco(request.getParameter("endereco"));
                cliente.setDataAniversario(new Date(request.getParameter("aniversario")));
                
                modelo.atualizar(cliente);
            }
            request.getRequestDispatcher("/cliente/list.jsp").forward(request, response);
       
        } catch(Exception ex) {
            System.out.println("Erro");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
