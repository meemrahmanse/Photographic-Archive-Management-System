package view;

import javax.swing.table.AbstractTableModel;

import model.Fotografia;

import java.util.List;
import java.util.ArrayList;


public class FotografiaTableModel extends AbstractTableModel {
	

	
	private final String[] colonne = {
		    "ID",
		    "Altezza",
		    "Larghezza",
		    "Stato",
		    "Soggetto"
		};

	
	 private List<Fotografia> lista;

	    public FotografiaTableModel(List<Fotografia> lista) {
	        this.lista = lista;
	    }

	    @Override
	    public int getRowCount() {
	        return lista.size();
	    }

	    @Override
	    public int getColumnCount() {
	        return colonne.length;
	    }


	    @Override
	    public Object getValueAt(int row, int column) {

	        Fotografia f = lista.get(row);

	        switch (column) {
	            case 0: return f.getIdFoto();
	            case 1: return f.getAltezza();
	            case 2: return f.getLarghezza();
	            case 3: return f.getStatoConservazione();
	            case 4: return f.getSoggetto();
	            default: return null;
	        }
	    }

	    
	    @Override
	    public String getColumnName(int column) {
	        return colonne[column];
	    }

	    public void aggiornaDati(List<Fotografia> nuovaLista) {
	        this.lista = nuovaLista;
	        fireTableDataChanged();
	    }

	
}
