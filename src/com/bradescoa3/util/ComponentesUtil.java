package com.bradescoa3.util;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.*;
import javax.swing.text.*;
import java.util.regex.*;

/**
 * Classe utilitária para componentes de interface gráfica.
 * Fornece métodos para validação e formatação de campos.
 */
public class ComponentesUtil {
    
    /**
     * Configura um campo de texto para aceitar apenas números.
     * 
     * @param textField O campo de texto a ser configurado
     * @param tamanhoMaximo O tamanho máximo permitido para o campo
     */
    public static void configurarCampoNumerico(JTextField textField, int tamanhoMaximo) {
        textField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                // Verifica se a string contém apenas números
                if (str == null || !str.matches("[0-9]+")) {
                    return;
                }
                
                // Verifica se o tamanho máximo não será excedido
                if ((getLength() + str.length()) <= tamanhoMaximo) {
                    super.insertString(offs, str, a);
                }
            }
        });
    }
    
    /**
     * Configura um campo de texto para aceitar apenas números e formatar como CPF/CNPJ.
     * 
     * @param textField O campo de texto a ser configurado
     */

    public static void configurarCampoCpfCnpj(JTextField textField) {
        AbstractDocument doc = (AbstractDocument) textField.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            private boolean isAdjusting = false;

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    replace(fb, offset, 0, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isAdjusting) return;
                isAdjusting = true;

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(currentText);
                sb.replace(offset, offset + length, text);
                String digits = sb.toString().replaceAll("[^0-9]", "");

                if (digits.length() > 14) digits = digits.substring(0, 14); // Máximo 14 dígitos

                String formatted = formatCpfCnpj(digits);
                fb.remove(0, fb.getDocument().getLength());
                fb.insertString(0, formatted, attrs);

                isAdjusting = false;
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                if (isAdjusting) return;
                isAdjusting = true;

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(currentText);
                sb.delete(offset, offset + length);
                String digits = sb.toString().replaceAll("[^0-9]", "");

                String formatted = formatCpfCnpj(digits);
                fb.remove(0, fb.getDocument().getLength());
                fb.insertString(0, formatted, null);

                isAdjusting = false;
            }

            private String formatCpfCnpj(String digits) {
                if (digits.length() <= 11) {
                    // CPF
                    if (digits.length() >= 10)
                        return digits.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{1,2})", "$1.$2.$3-$4");
                    else if (digits.length() >= 7)
                        return digits.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3");
                    else if (digits.length() >= 4)
                        return digits.replaceFirst("(\\d{3})(\\d+)", "$1.$2");
                    else
                        return digits;
                } else {
                    // CNPJ
                    return digits.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{1,2})?", "$1.$2.$3/$4-$5");
                }
            }
        });
    }

    
    /**
     * Configura um campo de texto para aceitar apenas números e formatar como moeda.
     * 
     * @param textField O campo de texto a ser configurado
     */
    public static void configurarCampoMonetario(JTextField textField) {
        // Adiciona um listener para formatar o valor conforme o usuário digita
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = textField.getText().replaceAll("[^0-9]", "");
                
                if (texto.isEmpty()) {
                    textField.setText("");
                    return;
                }
                
                // Converte para centavos
                double valor = Double.parseDouble(texto) / 100.0;
                
                // Formata como moeda
                String valorFormatado = String.format("R$ %.2f", valor);
                
                // Atualiza o texto do campo
                textField.setText(valorFormatado);
            }
        });
    }
    
    /**
     * Exibe uma mensagem de erro.
     * 
     * @param componente O componente pai para o diálogo
     * @param mensagem A mensagem de erro
     * @param titulo O título do diálogo
     */
    public static void exibirErro(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(componente, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Exibe uma mensagem de informação.
     * 
     * @param componente O componente pai para o diálogo
     * @param mensagem A mensagem de informação
     * @param titulo O título do diálogo
     */
    public static void exibirInformacao(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(componente, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exibe uma mensagem de alerta.
     * 
     * @param componente O componente pai para o diálogo
     * @param mensagem A mensagem de alerta
     * @param titulo O título do diálogo
     */
    public static void exibirAlerta(Component componente, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(componente, mensagem, titulo, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Exibe uma mensagem de confirmação.
     * 
     * @param componente O componente pai para o diálogo
     * @param mensagem A mensagem de confirmação
     * @param titulo O título do diálogo
     * @return true se o usuário confirmar, false caso contrário
     */
    public static boolean confirmar(Component componente, String mensagem, String titulo) {
        int resposta = JOptionPane.showConfirmDialog(componente, mensagem, titulo, 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }
}
