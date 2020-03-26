.data
	newLine: .asciiz "\n"
	messageIndex: .asciiz "; index: "
	messageHorsDomaine: .asciiz "ERREUR: valeur recherchée hors domaine: "
	messageTailleMax: .asciiz "taille de la liste: "
.text
main:

# Variables
#	Nom 	| Adresse ($s7) 
#------------------------------
#	x 	|   0



# creation des variables 
	move $s7, $sp
	add $sp, $sp, -4

# on réserve 4 bits par entier présent dans la TDS 

# chaque case d'un tableau prend 4 bits 


#-----------------------------------------------------------
#--------------------	Instructions :	--------------------
	

#----------------------- x := 5+3 

#-------- On récupère la valeur de 5 

# On mets 5 à $v0 
	li $v0 5

	add $sp, $sp -4		# Pour laisser de la place dans la pile
	sw  $v0, ($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)

#-------- On récupère la valeur de 3 

# On mets 3 à $v0 
	li $v0 3

	lw $v1, ($sp)		# Pour stocker la valeur à $sp dans $v1
	add $sp, $sp 4		# On remet la pile au bon endroit

# On fait l'addition de 5 et 3 
	add $v0, $v0 $v1

# On assigne x 
	sw $v0, 0($s7)	# on met la valeur de 5+3 dans x	

#----------------------- Ecrire x 


# On range la valeur de x dans $v0 
	lw $v0, 0($s7)

	li $v0, 1 	# on prépare l'affichage des variables
	lw $a0, 0($s7)

# on affiche x 
	syscall 	# ecrire

# Affichage du saut de ligne 
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

