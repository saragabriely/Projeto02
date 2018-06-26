package com.example.sara.projeto02;

import com.google.firebase.database.Exclude;

public class Write {

    private String chave;
    private String tag;
    private String conteudo;

    @Exclude
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Write() {
    }

    public Write(String conteudo) {
        setConteudo(conteudo);
    }

    public Write(String tag, String conteudo) {
        setTag(tag);
        setConteudo(conteudo);
    }

    public Write(String chave, String tag, String conteudo){
        this(tag, conteudo);
        setChave(chave);
    }

    @Override
    public String toString() {
        return "Tag: '" + tag + '\'' +
                " - Conteudo: '" + conteudo + '\'';
    }
    /*
    *  public String toString() {
        return "Write{" +
                " Tag: '" + tag + '\'' +
                " - Conteudo: '" + conteudo + '\'' +
                '}';
    }*/
}
