package com.sap.rfc;

import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.JCoStructure;

public class TableAdapterReader {
	protected JCoTable table;
	protected JCoStructure structure;

	protected String export;

	protected String imports;

	protected String structureName;
	
	protected String[] importMulti;
	
	protected String returns;
	
	public TableAdapterReader() {

	}

	public void setTable(JCoTable table){
		this.table = table;
	}

	public JCoTable getTable(){
		return this.table;
	}

	public void setStructure(JCoStructure structure){
		this.structure = structure;
	}

	public JCoStructure getStructure(){
		return this.structure;
	}

	public String get(String s) {
		return table.getValue(s) != null ? table.getValue(s).toString() : "";
	}

	public Boolean getBoolean(String s) {
		String value = table.getValue(s).toString();
		return value.equals("X");
	}

	public String getMessage() {
		return structure.getString("MESSAGE");
	}

	public String getResult() {
		return structure.getString("TYPE");
	}

	public int size() {
		return table.getNumRows();
	}

	public void next() {
		table.nextRow();
	}

	public String getExport() {
		return export;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public String getImports() {
		return imports;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}
	
	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	public String[] getImportMulti() {
		return importMulti;
	}

	public void setImportMulti(String[] importMulti) {
		this.importMulti = importMulti;
	}

	public String getReturns() {
		return returns;
	}

	public void setReturns(String returns) {
		this.returns = returns;
	}
}
