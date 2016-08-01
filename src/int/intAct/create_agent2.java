package intAct;
// Internal action code for project masReconV3




/*     */ 
/*     */ import jason.JasonException;
/*     */ import jason.architecture.AgArch;
/*     */ import jason.asSemantics.DefaultInternalAction;
/*     */ import jason.asSemantics.TransitionSystem;
/*     */ import jason.asSemantics.Unifier;
/*     */ import jason.asSyntax.ListTerm;
/*     */ import jason.asSyntax.StringTerm;
/*     */ import jason.asSyntax.StringTermImpl;
/*     */ import jason.asSyntax.Structure;
/*     */ import jason.asSyntax.Term;
/*     */ import jason.mas2j.ClassParameters;
/*     */ import jason.runtime.RuntimeServicesInfraTier;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SuppressWarnings("unused")
			public class create_agent2
/*     */   extends DefaultInternalAction
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*  96 */   public int getMinArgs() { return 1; }
/*  97 */   public int getMaxArgs() { return 3; }
/*     */   
/*     */   protected void checkArguments(Term[] args) throws JasonException {
/* 100 */     super.checkArguments(args);
/* 101 */     if ((args.length > 1) && (!args[1].isString()))
/* 102 */       throw JasonException.createWrongArgument(this, "second argument must be a string");
/* 103 */     if ((args.length == 3) && (!args[2].isList())) {
/* 104 */       throw JasonException.createWrongArgument(this, "third argument must be a list");
/*     */     }
/*     */   }
/*     */   
/*     */   public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
/* 109 */     checkArguments(args);
/*     */     
/* 111 */     String name = getName(args);
/* 112 */     String source = getSource(args);
/*     */     
/* 114 */     List<String> agArchClasses = getAgArchClasses(args);
/*     */     
/* 116 */     String agClass = null;
/* 117 */     ClassParameters bbPars = null;
/* 118 */     if (args.length > 2)
/*     */     {
/* 120 */       for (Term t : (ListTerm)args[2]) {
/* 121 */         if (t.isStructure()) {
/* 122 */           Structure s = (Structure)t;
/* 123 */           if (s.getFunctor().equals("beliefBaseClass")) {
/* 124 */             bbPars = new ClassParameters(testString(s.getTerm(0)));
/* 125 */           } else if (s.getFunctor().equals("agentClass")) {
/* 126 */             agClass = testString(s.getTerm(0)).toString();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 131 */     RuntimeServicesInfraTier rs = ts.getUserAgArch().getRuntimeServices();
/* 132 */     name = rs.createAgent(name, source, agClass, agArchClasses, bbPars, ts.getSettings());
/* 133 */     rs.startAgent(name);
/*     */     
/* 135 */     if (args[0].isVar()) {
/* 136 */       return Boolean.valueOf(un.unifies(new StringTermImpl(name), args[0]));
/*     */     }
/* 138 */     return Boolean.valueOf(true);
/*     */   }
/*     */   
/*     */   protected String getName(Term[] args) {
	 		  String name;
/*     */     //String name;
/* 143 */     if (args[0].isString()) {
/* 144 */       name = ((StringTerm)args[0]).getString();
/*     */     } else {
/* 146 */       name = args[0].toString();
/*     */     }
/* 148 */     if (args[0].isVar())
/* 149 */       name = name.substring(0, 1).toLowerCase() + name.substring(1);
/* 150 */     return name;
/*     */   }
/*     */   
/*     */   protected String getSource(Term[] args) throws JasonException {
/* 154 */     String source = null;
/* 155 */     if (args.length > 1)
/*     */     {
/* 157 */       File fSource = new File(((StringTerm)args[1]).getString());
/* 158 */       if (!fSource.exists()) {
/* 159 */         fSource = new File("src/asl/" + ((StringTerm)args[1]).getString());
/* 160 */         if (!fSource.exists()) {
/* 161 */           fSource = new File("src/agt/" + ((StringTerm)args[1]).getString());
/* 162 */           if (!fSource.exists()) {
/* 163 */              fSource = new File("src/agt/participantBus.asl");
 					  // System.out.println("path Interna Action -------> src/agt/participantBus.asl");
/*     */           }
/*     */         }
/*     */       }
/* 167 */       source = fSource.getAbsolutePath();
/*     */     }
/* 169 */     return source;
/*     */   }
/*     */   
/*     */   protected List<String> getAgArchClasses(Term[] args) {
/* 173 */     List<String> agArchClasses = new ArrayList<String>();
/* 174 */     if (args.length > 2)
/*     */     {
/* 176 */       for (Term t : (ListTerm)args[2]) {
/* 177 */         if (t.isStructure()) {
/* 178 */           Structure s = (Structure)t;
/* 179 */           if (s.getFunctor().equals("agentArchClass")) {
/* 180 */             agArchClasses.add(testString(s.getTerm(0)).toString());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 185 */     return agArchClasses;
/*     */   }
/*     */   
/*     */   private Structure testString(Term t) {
/* 189 */     if (t.isStructure())
/* 190 */       return (Structure)t;
/* 191 */     if (t.isString())
/* 192 */       return Structure.parse(((StringTerm)t).getString());
/* 193 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\matheus\Desktop\TESE\JaCaMo files\lib\jason.jar
 * Qualified Name:     jason.stdlib.create_agent
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */