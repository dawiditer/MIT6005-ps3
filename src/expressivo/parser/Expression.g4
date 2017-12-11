/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

grammar Expression;
import Configuration;

root : stat EOF ;
stat : expr ;
expr :
     expr MULT expr      #Mult
     | expr ADD  expr    #Add
     | ID                #Id
     | NUM               #Num
     | '(' expr ')'      #Brackets
     ;
ID   : [a-zA-Z]+ ;
NUM  : [0-9]+ ('.' [0-9]+)?;
MULT : '*' ;
ADD  : '+' ;
NEWLINE : '\n' ;
WS   : [ \t]+ -> skip ;
