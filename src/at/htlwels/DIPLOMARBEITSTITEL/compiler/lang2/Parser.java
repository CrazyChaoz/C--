package at.htlwels.DIPLOMARBEITSTITEL.compiler.lang2;

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.*;
import at.htlwels.DIPLOMARBEITSTITEL.ui.CLI;

import java.util.List;
import java.util.ArrayList;


public class Parser implements at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Parser{
	public static final int _EOF = 0;
	public static final int _ident = 1;
	public static final int _intCon = 2;
	public static final int _floatCon = 3;
	public static final int _charCon = 4;
	public static final int _stringCon = 5;
	public static final int _lpar = 6;
	public static final int _rpar = 7;
	public static final int _semicolon = 8;
	public static final int _assign = 9;
	public static final int _eql = 10;
	public static final int _neq = 11;
	public static final int _lss = 12;
	public static final int _leq = 13;
	public static final int _gtr = 14;
	public static final int _geq = 15;
	public static final int _bang = 16;
	public static final int _mul = 17;
	public static final int _div = 18;
	public static final int _add = 19;
	public static final int _substr = 20;
	public static final int maxT = 48;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	public  SymbolTable     symbolTable;                     // symbol table
    public int foreachVariable=0;

//--- LL(1) conflict resolvers

	// Returns true if a VarDecl comes next in the input
	boolean isVarDecl() {
		if (la.kind == _ident) {
			Token x = scanner.Peek();
			while (x.kind != _semicolon) {
				if (x.kind == _EOF || x.kind == _lpar || x.kind == _assign) return false;
				x = scanner.Peek();
			}
			return true;
		}
		return false;
	}

	// Returns true if the next input is an Expr and not a '(' Condition ')'
	boolean isExpr() {
		if (la.kind == _bang) return false;
		else if (la.kind == _lpar) {
			Token x = scanner.Peek();
			while (x.kind != _rpar && x.kind != _EOF) {
				if (x.kind == _eql || x.kind == _neq || x.kind == _lss || x.kind == _leq || x.kind == _gtr || x.kind == _geq) return false;
				x = scanner.Peek();
			}
			return x.kind == _rpar;
		} else return true;
	}

	// Returns true if the next input is a type cast
	boolean isCast() {
    		Token x = scanner.Peek();
    		if (x.kind != _ident) return false;
    		Obj obj = symbolTable.lookup(x.val);
    		return obj.kind == ObjKind.TYPE;
    	}


    boolean isCall(){
        scanner.ResetPeek();
        Token x = la;
        if(x.kind != _ident)
            return false;
        x=scanner.Peek();
        if(x.kind != _lpar)
            return false;

        return true;
    }
    public SymbolTable getSymbolTable() {
    		return symbolTable;
    	}


    public boolean hasErrors(){
        return this.errors.count!=0;
    }

    public void addError(String message){
            this.errors.count++;
            //TODO: make error message do somethig
        }


//##################################################################################



	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Lang2() {
		symbolTable = new SymbolTable(this);
		while (la.kind == 21) {
			ImportDecl();
		}
		while (la.kind == 22 || la.kind == 25 || la.kind == 29) {
			if (la.kind == 25) {
				ConstDecl();
			} else if (la.kind == 22) {
				StructDecl();
			} else {
				ProcDecl();
			}
		}
	}

	void ImportDecl() {
		Expect(21);
		Expect(5);
		symbolTable.mergeWithSymboltable(CLI.parseAndStripFile(this.t.val));
	}

	void ConstDecl() {
		Expect(25);
		Type type = Type();
		String name = Ident();
		Obj o=new Obj(ObjKind.CON,name,type);
		Expect(9);
		if (la.kind == 2) {
			Get();
			o.val=symbolTable.intVal(this.t.val);if(type!=symbolTable.intType)this.errors.count++;
		} else if (la.kind == 3) {
			Get();
			o.fVal=symbolTable.floatVal(this.t.val);if(type!=symbolTable.floatType)this.errors.count++;
		} else if (la.kind == 4) {
			Get();
			o.val=symbolTable.charVal(this.t.val);if(type!=symbolTable.charType)this.errors.count++;
		} else if (la.kind == 5) {
			Get();
			o.strVal=this.t.val;if(type!=symbolTable.stringType)this.errors.count++;
		} else SynErr(49);
		symbolTable.insert(o);
		lineending();
	}

	void StructDecl() {
		Expect(22);
		String name = Ident();
		Expect(23);
		Type type=new Type(Type.STRUCT);Obj obj=new Obj(ObjKind.TYPE,name,type);symbolTable.insert(obj);symbolTable.openScope(type.fields);
		while (la.kind == 1) {
			VarDecl();
		}
		symbolTable.closeScope();type.size=type.fields.size;
		Expect(24);
	}

	void ProcDecl() {
		Obj procedure=null; NodeList nodeList=new NodeList();List<Obj> args=new ArrayList(); Type type=symbolTable.noType;
		Expect(29);
		String name = Ident();
		Expect(6);
		if (la.kind == 1 || la.kind == 32) {
			Obj param1 = Parameter();
			args.add(param1); 
			while (la.kind == 26) {
				Get();
				Obj paramN = Parameter();
				args.add(paramN); 
			}
		}
		Expect(7);
		if (la.kind == 30) {
			Get();
			type = Type();
		}
		procedure=new Obj(ObjKind.PROC,name,type);symbolTable.openScope(procedure.localScope); int startLine=scanner.line;
		if (la.kind == 23) {
			for(Obj parameter:args)
			   symbolTable.insert(parameter);
			
			procedure.nPars=args.size();
			
			Obj forwardProc=symbolTable.lookup(name);
			if(forwardProc!=symbolTable.noObj){
			   /*forward declerated or error*/
			   if(!symbolTable.checkForwardParams(forwardProc,procedure)){
			       System.err.println("Wrong Parameters");
			       this.errors.count++;
			   }
			   procedure.isForward=true;
			}
			
			Get();
			while (StartOf(1)) {
				if (isVarDecl()) {
					VarDecl();
				} else {
					Node statement = Statement();
					nodeList.add(statement);
				}
			}
			procedure.ast=new Node(NodeKind.STATSEQ,nodeList.get(),new Node(NodeKind.TRAP,null,null,scanner.line),procedure.type);
			Expect(24);
		} else if (la.kind == 8) {
			Get();
			Expect(31);
			Expect(8);
		} else SynErr(50);
		symbolTable.closeScope();symbolTable.insert(procedure);
	}

	void lineending() {
		Expect(8);
	}

	String  Ident() {
		String  name;
		Expect(1);
		name=this.t.val;
		return name;
	}

	void VarDecl() {
		Type type = Type();
		String name = Ident();
		symbolTable.insert(ObjKind.VAR,name,type);
		while (la.kind == 26) {
			Get();
			String name1 = Ident();
			symbolTable.insert(ObjKind.VAR,name1,type);
		}
		lineending();
	}

	Type  Type() {
		Type  type;
		String name = Ident();
		type=symbolTable.find(name).type; 
		while (la.kind == 27) {
			Get();
			Expect(2);
			type=new Type(Type.ARR,symbolTable.intVal(this.t.val),type);
			Expect(28);
		}
		return type;
	}

	Obj  Parameter() {
		Obj  obj;
		boolean isRef=false;
		if (la.kind == 32) {
			Get();
			isRef=true;
		}
		Type type = Type();
		String name = Ident();
		obj=new Obj(ObjKind.VAR,name,type);obj.isRef=isRef;
		return obj;
	}

	Node  Statement() {
		Node  statement;
		statement=null;Node expression=null; Node designator=null;NodeList innerStatements=new NodeList();
		if (isCall()) {
			statement = ProcCall();
		} else if (la.kind == 1) {
			designator = Designator();
			Expect(9);
			expression = Expr();
			statement=new Node(NodeKind.ASSIGN,designator,expression,scanner.line);if(designator.type!=expression.type)this.errors.count++;
			lineending();
		} else if (la.kind == 41) {
			statement = ReturnStatement();
		} else if (la.kind == 8) {
			lineending();
		} else if (la.kind == 33) {
			Get();
			expression = Expr();
			lineending();
			statement=new Node(NodeKind.PRINT,expression,null,scanner.line);
		} else if (la.kind == 34) {
			Get();
			String variable = Ident();
			if (la.kind == 35) {
				Get();
			} else if (la.kind == 13) {
				Get();
			} else if (la.kind == 36) {
				Get();
			} else SynErr(51);
			designator = Designator();
			Node everyTime = Statement();
			if(designator.type.kind==Type.ARR){
			   //Sin.commit(new Sin("ERROR -- no array type to iterate over"));
			//else{
			
			NodeList ifBody=new NodeList();
			NodeList whileBody=new NodeList();
			
			Obj iteratorObj=new Obj(ObjKind.VAR,("$iterator" + foreachVariable++),symbolTable.intType);
			symbolTable.insert(iteratorObj);
			symbolTable.insert(ObjKind.VAR,variable,designator.type.elemType);
			
			ifBody.add(new Node(NodeKind.ASSIGN,new Node(iteratorObj),new Node(0),scanner.line));
			
			
			
			//whileBody.add(new Node(NodeKind.IF,new Node(NodeKind.AND,condition,new Node(NodeKind.NEQ,new Node(NodeKind.INDEX,designator,new Node(iteratorObj),0),new Node(0),0),Type.BOOL),everyTime,scanner.line));
			whileBody.add(new Node(NodeKind.IF,new Node(NodeKind.NEQ,new Node(NodeKind.INDEX,designator,new Node(iteratorObj),designator.type.elemType),new Node(0),Type.BOOL),everyTime,scanner.line));
			whileBody.add(new Node(NodeKind.ASSIGN,new Node(iteratorObj),new Node(NodeKind.PLUS,new Node(iteratorObj),new Node(1),symbolTable.intType),scanner.line));
			
			ifBody.add(new Node(NodeKind.WHILE,new Node(NodeKind.LSS,new Node(iteratorObj),new Node(designator.type.elements),Type.BOOL),whileBody.get(),scanner.line));
			
			statement=ifBody.get();
			
			}
			
			
		} else if (la.kind == 37) {
			Get();
			Node oneTime = Statement();
			Node condition = Condition();
			Expect(8);
			Node everyTime = Statement();
			Expect(23);
			while (StartOf(1)) {
				Node innerStatement = Statement();
				innerStatements.add(innerStatement); 
			}
			innerStatements.add(everyTime);statement=innerStatements.get(); 
			Expect(24);
			NodeList forStuff=new NodeList();forStuff.add(oneTime);forStuff.add(new Node(NodeKind.WHILE,condition,statement,scanner.line));statement=forStuff.get();/*statement=new Node(NodeKind.FOR,oneTime,new Node(NodeKind.WHILE,condition,statement,scanner.line),scanner.line);*/
		} else if (la.kind == 38) {
			Get();
			Node condition = Condition();
			Node innerStatement = Statement();
			statement=new Node(NodeKind.WHILE,condition,innerStatement,scanner.line);
		} else if (la.kind == 39) {
			Get();
			Node condition = Condition();
			Node ifStatements = Statement();
			Node ifElse=ifStatements;
			if (la.kind == 40) {
				Get();
				Node elseStatements = Statement();
				ifElse=new Node(NodeKind.IFELSE,ifElse,elseStatements,scanner.line);
			}
			statement=new Node(NodeKind.IF,condition,ifElse,scanner.line);
		} else if (la.kind == 23) {
			Get();
			while (StartOf(1)) {
				Node innerStatement = Statement();
				innerStatements.add(innerStatement); 
			}
			statement=innerStatements.get(); 
			Expect(24);
		} else SynErr(52);
		return statement;
	}

	Node  ProcCall() {
		Node  statement;
		NodeList nodeList=new NodeList();boolean isRef=false; int parCount=0;
		String name = Ident();
		Expect(6);
		while (la.kind == 1 || la.kind == 32) {
			if (la.kind == 32) {
				Get();
				isRef=true;
			}
			Node designator = Designator();
			if(isRef)designator.obj.isRef=true;nodeList.add(designator);parCount++; 
		}
		Expect(7);
		Obj proc=symbolTable.find(name);statement=new Node(NodeKind.CALL,new Node(proc),nodeList.get(),scanner.line);statement.type=proc.type;if(parCount!=proc.nPars)this.errors.count++;
		return statement;
	}

	Node  Designator() {
		Node  designator;
		String name = Ident();
		designator=new Node(symbolTable.find(name));
		while (la.kind == 27 || la.kind == 44) {
			if (la.kind == 44) {
				Get();
				String name2 = Ident();
				Obj typeVar=symbolTable.findField(name2,designator.type); designator=new Node(NodeKind.DOT,designator,new Node(typeVar),typeVar.type );
			} else {
				Get();
				Node expr = Expr();
				designator=new Node(NodeKind.INDEX,designator,expr,designator.type.elemType ); 
				Expect(28);
			}
		}
		return designator;
	}

	Node  Expr() {
		Node  expr;
		Node term;NodeKind kind;
		expr = Term();
		while (la.kind == 19 || la.kind == 20) {
			kind = Addop();
			term = Term();
			expr=new Node(kind,expr,term,term.type==expr.type?term.type:SymbolTable.noType);
		}
		return expr;
	}

	Node  ReturnStatement() {
		Node  statement;
		statement=null; Node expression=null;
		Expect(41);
		if (StartOf(2)) {
			expression = Expr();
		}
		statement=new Node(NodeKind.RETURN,expression,null,scanner.line);
		lineending();
		return statement;
	}

	Node  Condition() {
		Node  cond;
		cond = CondTerm();
		while (la.kind == 46) {
			Get();
			Node cond2 = CondTerm();
			cond=new Node(NodeKind.OR,cond,cond2,Type.BOOL); 
		}
		return cond;
	}

	Node  Term() {
		Node  term;
		Node faktor;NodeKind kind;
		term = Factor();
		while (la.kind == 17 || la.kind == 18 || la.kind == 45) {
			kind = Mulop();
			faktor = Factor();
			term=new Node(kind,term,faktor,faktor.type==term.type?faktor.type:SymbolTable.noType); 
		}
		return term;
	}

	NodeKind  Addop() {
		NodeKind  kind;
		kind=null;
		if (la.kind == 19) {
			Get();
			kind=NodeKind.PLUS;
		} else if (la.kind == 20) {
			Get();
			kind=NodeKind.MINUS; 
		} else SynErr(53);
		return kind;
	}

	Node  Factor() {
		Node  faktor;
		faktor=null;String name;
		if (isCall()) {
			faktor = ProcCall();
		} else if (la.kind == 1) {
			faktor = Designator();
		} else if (la.kind == 2) {
			Get();
			faktor=new Node(symbolTable.intVal(this.t.val)); 
		} else if (la.kind == 3) {
			Get();
			faktor=new Node(symbolTable.floatVal(this.t.val)); 
		} else if (la.kind == 5) {
			Get();
			faktor=new Node(this.t.val); 
		} else if (la.kind == 4) {
			Get();
			faktor=new Node(symbolTable.charVal(this.t.val)); 
		} else if (la.kind == 42) {
			Get();
			faktor=new Node(NodeKind.READINT,null,null,symbolTable.intType);
		} else if (la.kind == 43) {
			Get();
			faktor=new Node(NodeKind.READCHAR,null,null,symbolTable.charType);
		} else if (la.kind == 20) {
			Get();
			Node faktor1 = Factor();
			faktor=new Node(NodeKind.MINUS,new Node(0),faktor1,faktor1.type);
		} else if (isCast()) {
			Expect(6);
			Type type = Type();
			Expect(7);
			Node factor = Factor();
			faktor=new Node(
			factor.type==symbolTable.intType&&type==symbolTable.charType?NodeKind.I2C:
			factor.type==symbolTable.intType&&type==symbolTable.floatType?NodeKind.I2F:
			factor.type==symbolTable.floatType&&type==symbolTable.intType?NodeKind.F2I:
			factor.type==symbolTable.charType&&type==symbolTable.intType?NodeKind.C2I:
			NodeKind.GENERIC_CAST,
			factor,null,type);
			
		} else if (la.kind == 6) {
			Get();
			faktor = Expr();
			Expect(7);
		} else SynErr(54);
		return faktor;
	}

	NodeKind  Mulop() {
		NodeKind  kind;
		kind=null;
		if (la.kind == 17) {
			Get();
			kind=NodeKind.TIMES;
		} else if (la.kind == 18) {
			Get();
			kind=NodeKind.DIV;
		} else if (la.kind == 45) {
			Get();
			kind=NodeKind.REM;
		} else SynErr(55);
		return kind;
	}

	Node  CondTerm() {
		Node  cond;
		cond = CondFact();
		while (la.kind == 47) {
			Get();
			Node cond2 = CondFact();
			cond=new Node(NodeKind.AND,cond,cond2,Type.BOOL); 
		}
		return cond;
	}

	Node  CondFact() {
		Node  cond;
		cond=null;
		if (isExpr()) {
			Node expr1 = Expr();
			NodeKind which = Relop();
			Node expr2 = Expr();
			cond=new Node(which,expr1,expr2,Type.BOOL); 
		} else if (la.kind == 16) {
			Get();
			Expect(6);
			Node cond1 = Condition();
			Expect(7);
			cond=new Node(NodeKind.NOT,cond1,null,Type.BOOL);
		} else if (la.kind == 6) {
			Get();
			Node cond1 = Condition();
			Expect(7);
			cond=cond1;
		} else SynErr(56);
		return cond;
	}

	NodeKind  Relop() {
		NodeKind  which;
		which=null;
		switch (la.kind) {
		case 10: {
			Get();
			which=NodeKind.EQL;
			break;
		}
		case 11: {
			Get();
			which=NodeKind.NEQ;
			break;
		}
		case 14: {
			Get();
			which=NodeKind.GTR;
			break;
		}
		case 15: {
			Get();
			which=NodeKind.GEQ;
			break;
		}
		case 12: {
			Get();
			which=NodeKind.LSS;
			break;
		}
		case 13: {
			Get();
			which=NodeKind.LEQ;
			break;
		}
		default: SynErr(57); break;
		}
		return which;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Lang2();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x},
		{_x,_T,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_T, _x,_x,_x,_x, _x,_x,_x,_x, _x,_T,_T,_x, _x,_T,_T,_T, _x,_T,_x,_x, _x,_x,_x,_x, _x,_x},
		{_x,_T,_T,_T, _T,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x,_x,_x, _x,_x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "intCon expected"; break;
			case 3: s = "floatCon expected"; break;
			case 4: s = "charCon expected"; break;
			case 5: s = "stringCon expected"; break;
			case 6: s = "lpar expected"; break;
			case 7: s = "rpar expected"; break;
			case 8: s = "semicolon expected"; break;
			case 9: s = "assign expected"; break;
			case 10: s = "eql expected"; break;
			case 11: s = "neq expected"; break;
			case 12: s = "lss expected"; break;
			case 13: s = "leq expected"; break;
			case 14: s = "gtr expected"; break;
			case 15: s = "geq expected"; break;
			case 16: s = "bang expected"; break;
			case 17: s = "mul expected"; break;
			case 18: s = "div expected"; break;
			case 19: s = "add expected"; break;
			case 20: s = "substr expected"; break;
			case 21: s = "\"import\" expected"; break;
			case 22: s = "\"struct\" expected"; break;
			case 23: s = "\"{\" expected"; break;
			case 24: s = "\"}\" expected"; break;
			case 25: s = "\"const\" expected"; break;
			case 26: s = "\",\" expected"; break;
			case 27: s = "\"[\" expected"; break;
			case 28: s = "\"]\" expected"; break;
			case 29: s = "\"def\" expected"; break;
			case 30: s = "\":\" expected"; break;
			case 31: s = "\"forward\" expected"; break;
			case 32: s = "\"ref\" expected"; break;
			case 33: s = "\"print\" expected"; break;
			case 34: s = "\"foreach\" expected"; break;
			case 35: s = "\"<-\" expected"; break;
			case 36: s = "\"in\" expected"; break;
			case 37: s = "\"for\" expected"; break;
			case 38: s = "\"while\" expected"; break;
			case 39: s = "\"if\" expected"; break;
			case 40: s = "\"else\" expected"; break;
			case 41: s = "\"return\" expected"; break;
			case 42: s = "\"readInt\" expected"; break;
			case 43: s = "\"readChar\" expected"; break;
			case 44: s = "\".\" expected"; break;
			case 45: s = "\"%\" expected"; break;
			case 46: s = "\"||\" expected"; break;
			case 47: s = "\"&&\" expected"; break;
			case 48: s = "??? expected"; break;
			case 49: s = "invalid ConstDecl"; break;
			case 50: s = "invalid ProcDecl"; break;
			case 51: s = "invalid Statement"; break;
			case 52: s = "invalid Statement"; break;
			case 53: s = "invalid Addop"; break;
			case 54: s = "invalid Factor"; break;
			case 55: s = "invalid Mulop"; break;
			case 56: s = "invalid CondFact"; break;
			case 57: s = "invalid Relop"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
