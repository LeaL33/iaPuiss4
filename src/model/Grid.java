package model;

import javafx.scene.paint.Color;

public class Grid {
	
	final static int NB_LINES = 6;
	final static int NB_COLUMNS = 7;
	
	Chip[][] chips;
	public int nbLines,nbColumns;
	
	int nbChips;
	
	
	/** Initialisation du plateau
	 * pion[i][j] = null (aucun pion dans la case)
	 */
	
	public Grid(){
		// valeurs par défaut utilisées si pas précisés
		this(Grid.NB_LINES,Grid.NB_COLUMNS);
		
	}
	public Grid(int row, int col){
		
		this.nbLines = row;
		this.nbColumns = col;		
		
		chips = new Chip[nbLines][nbColumns];
		
		int i,j;
		for(i=0; i<this.nbLines;i++)
			for(j=0;j<this.nbColumns; j++)
				this.chips[i][j]=null;
		
		nbChips = 0;
	}
	
	public Chip[][] getData(){
		return this.chips;
	}
	
	public Chip getCase(Position p){
		return this.chips[p.getRow()][p.getCol()];
	}
	
	public Color[][] getDataView(){
		Color[][] dataView = new Color[this.nbLines][this.nbColumns];
		
		for(int i=0; i<this.nbLines;i++){
			for(int j=0;j<this.nbColumns; j++){
				if(this.chips[i][j] == null)
					dataView[i][j] = null;
				else
					dataView[i][j] = this.chips[i][j].getPlayer().getColor();
			}
		}
		return dataView;
	}
	
	// Retour l'id de la ligne où le jeton a été déposé
	public int add(int c, Chip p){
		int line=0;;
		int l=0;
		
		while(l<this.nbLines ){
			if(isCaseFree(l,c)){				
				line=l;
				break;				
			}
			
			l++;
		}
		
		this.add(line,c, p);
		return line;
			
		
		
		
	}
	
	
	
	// 
	public void add(int l, int c, Chip p){
		
		try{
		this.chips[l][c] = p;
		}
		catch(Exception e){
			System.err.println("ERREUR :"+e+" "+l+"-"+c);
		}
		
		nbChips++;
	}
	public void remove(int l, int c){
		this.chips[l][c] = null;
		nbChips--;
	}
	
	public boolean isColumnFull(int col){
		return this.chips[this.chips.length -1][col] != null;
	}
	
	public boolean isCaseFree(int l, int c){
		//System.out.println("L="+l+"C="+c);
		if(l>= 0 && l < nbLines && c >= 0 && c < nbColumns)
			return this.chips[l][c] == null;
		return false;
	}
	
	public boolean isFull(){

		
		for(int j=0;j<this.nbColumns; j++){
			if(this.chips[this.nbLines-1][j] == null )
				return false;
		}
		
		return true;		
		
	}
	
	public int getnbChips(){
		return nbChips;
	}
	

	
	
	
	int nbAlignmentForWinning = 4;
	
	
	public String getStateLines(){
		return getStateVerticalLine()+"|"
				+getStateHorizontalLine()+"|"
				+getStateDiagonal1Line()+"|"
				+getStateDiagonal2Line();
	}
	
	public String getSymbol(int row, int col){
		
		Chip currentChip = this.chips[row][col];
		
		if(isCaseFree(row,col))
			return "_";
		else{
			return currentChip.getPlayer().getSymbol();			
		}
		
		
	}
	
	public String getStateVerticalLine(){
		String str="";		
		
		for(int j=0; j<nbColumns;j++){
			for(int i=0; i<nbLines ;i++){
				str+= getSymbol(i,j);		
			}
			str+=",";
		}
		
		return str;
	}
	
	public String getStateHorizontalLine(){
		String str="";		
		
		for(int i=0;i<nbLines;i++){
			for(int j=0; j<nbColumns ;j++){
				str+= getSymbol(i,j);		
			}
			str+=",";
		}
		
		return str;
	}
	
	public String getStateDiagonal1Line(){
		String str="";
		
		
		
		for(int row=0;row<nbLines;row++){			
			for(int k=0; k<nbColumns && row+k< nbLines;  k++){
				str+= getSymbol(row+k,k);
			}
			str+=",";
		}
		
		for(int col=1;col<nbColumns;col++){			
			for(int k=0; k<nbLines && col+k< nbColumns;  k++){
				str+= getSymbol(k,col+k);				
			}
			str+=",";
		}
		
		
		return str;		
		
	}
	
	public String getStateDiagonal2Line(){
		String str="";
		
		
		
		for(int row=0;row<nbLines;row++){			
			for(int k=0; k<nbColumns && row+k< nbLines;  k++){
				str+= getSymbol(row+k,nbColumns-1 - k);
			}
			str+=",";
		}
		
		for(int col=1;col<nbColumns;col++){			
			for(int k=0; k<nbLines && col+k< nbColumns;  k++){
				str+= getSymbol(k,nbColumns-1 - col - k);				
			}
			str+=",";
		}
		
		
		return str;		
		
	}
	
	public String getStateDiagonal2Line(Position p){
		String str="";		
		Position posStart;
		int pos_row = p.getRow();
		int pos_col = p.getCol();
		
		// Cherche le départ de la diagonale en bas à droite
		posStart = p;
		for(int i=0; pos_row -i >= 0 && pos_col+i < nbColumns ;i++)		
			posStart = new Position(pos_row-i,pos_col+i);			
				
		
		pos_row = posStart.getRow();
		pos_col = posStart.getCol();
		
		for(int i=0; pos_row+i < nbLines && pos_col-i >=0 ;i++){
			str+= getSymbol(pos_row+i,pos_col-i);		
		}
		
		return str;
		
		
	}
	
	public QualityMove verticalAlignment(Position p, Player player){
		
		String str;
		QualityMove qm = new QualityMove();
		
		int pos_row = p.getRow();
		int pos_col = p.getCol();
		
		for(int i=0; i<nbLines ;i++){
			
			Chip currentChip = this.chips[i][pos_col];
			
			if(isCaseFree(i,pos_col)){
				qm.incFree();
			}			
			else if(!isCaseFree(i,pos_col) && currentChip.getPlayer() != player){
				qm.incChipOppPlayer();
			}
			else{				
				qm.incChipCurPlayer();
			}	
			
			
			
		}
		
		qm.end();
		//if(pos_col == 2)
			//this.showDebug();
		//System.out.println("Test align vertical - "+p+" - "+player.getId()+" : "+qm);
		
		return qm;		
		
	}
	
	public QualityMove horizontalAlignment(Position p, Player player){
		
		
		QualityMove qm = new QualityMove();
		
		int pos_row = p.getRow();
		int pos_col = p.getCol();
		
		
		for(int i=0; i<nbColumns ;i++){
			
			Chip currentChip = this.chips[pos_row][i];
			
			if(isCaseFree(pos_row,i)){
				qm.incFree();
			}			
			else if(!isCaseFree(pos_row,i) && currentChip.getPlayer() != player){
				qm.incChipOppPlayer();
			}
			else{				
				qm.incChipCurPlayer();
			}	
			
		}
		
		qm.end();
		
		
			
		//System.out.println("Test align horizontal - "+p+" - "+player.getId()+" : "+qm);	
			
			
		return qm;		
	}
	
	public QualityMove diagonalAlignment(Position p, Player player){
		
		Position posStart;
		QualityMove qm = new QualityMove();
		
		int pos_row = p.getRow();
		int pos_col = p.getCol();
		
		
		// Cherche le départ de la diagonale en bas à gauche
		posStart = p;
		for(int i=0; pos_row -i >= 0 && pos_col-i >= 0 ;i++)		
			posStart = new Position(pos_row-i,pos_col-i);			
				
		
		pos_row = posStart.getRow();
		pos_col = posStart.getCol();
		
		for(int i=0; pos_row+i < nbLines && pos_col+i<nbColumns ;i++){
			
			Chip currentChip = this.chips[pos_row+i][pos_col+i];
			
			if(isCaseFree(pos_row+i,pos_col+i)){
				qm.incFree();
			}			
			else if(!isCaseFree(pos_row+i,pos_col+i) && currentChip.getPlayer() != player){
				qm.incChipOppPlayer();
			}
			else{				
				qm.incChipCurPlayer();
			}	
			
		}
		
		qm.end();
		
		pos_row = p.getRow();
		pos_col = p.getCol();
		
		// Cherche le départ de la diagonale en bas à droite
		posStart = p;
		for(int i=0; pos_row -i >= 0 && pos_col+i < nbColumns ;i++)		
			posStart = new Position(pos_row-i,pos_col+i);			
				
		
		pos_row = posStart.getRow();
		pos_col = posStart.getCol();
		
		for(int i=0; pos_row+i < nbLines && pos_col-i >=0 ;i++){
			
			Chip currentChip = this.chips[pos_row+i][pos_col-i];
			
			if(isCaseFree(pos_row+i,pos_col-i)){
				qm.incFree();
			}			
			else if(!isCaseFree(pos_row+i,pos_col-i) && currentChip.getPlayer() != player){
				qm.incChipOppPlayer();
			}
			else{				
				qm.incChipCurPlayer();
			}	
			
		}
		
		qm.end();
		
		
		return qm;
		
		
	}
	
	
	public void showDebug(){
		
		int i,j;
		String player="";
		System.out.printf("Plateau :\n");
		for(i=nbLines-1; i>=0; i--){
			for(j=0; j<nbColumns; j++){
				if(!this.isCaseFree(i, j) ){
					
					player = this.chips[i][j].getPlayer().getSymbol();						
				}
				else
					player = "-";
					
					System.out.printf("%3s ", player);
			}
			
			System.out.printf("\n");
			
		}
	
	}
}
