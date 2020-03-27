.data
	newLine: .asciiz "\n"
	messageIndex: .asciiz "; index: "
	messageHorsDomaine: .asciiz "ERREUR: valeur recherch�e hors domaine: "
	messageTailleMax: .asciiz "taille de la liste: "
.text
main:

# Variables
#	Nom 	| Adresse ($s7) 
#------------------------------
#	a 	|   0



# creation des variables 
	move $s7, $sp
	add $sp, $sp, -4

# on r�serve 4 bits par entier pr�sent dans la TDS 

# chaque case d'un tableau prend 4 bits 


#-----------------------------------------------------------
#--------------------	Instructions :	--------------------
	

#----------------------- a := 7 

# Calcul de la valeur de l'expression dans $v0 

# On met 7 � $v0 
	li $v0 7

# On empile 
	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 � $sp (le haut de la pile)
	add $sp, $sp, -4	# Pour laisser de la place dans la pile
	la $a0, 0($s7)


# On depile 
	add $sp, $sp 4	# On remet la pile au bon endroit
	lw $v0, 0($sp)	# Pour stocker la valeur � $sp dans $v1

# On range $v0 � $a0 
	sw $v0, 0($a0)
	
# Affichage du saut de ligne 
	lw $a0, 0($s7)
	li $v0, 1
	syscall

	li $v0, 4
	la $a0, newLine
	syscall


#------------------	Fin instructions :	------------------
#-----------------------------------------------------------------


#----------------------------

# Fonctions pour terminer le programme 
exit:
	 li $v0, 17	#code de sortie du programme
	 syscall

