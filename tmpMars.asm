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
#	a 	|  -4
#	b 	| -24
#	c 	|   0



# creation des variables 
	move $s7, $sp
	add $sp, $sp, -44

# on réserve 4 bits par entier présent dans la TDS 

# chaque case d'un tableau prend 4 bits 


#-----------------------------------------------------------
#--------------------	Instructions :	--------------------
	

#----------------------- c := 2 

# Calcul de la valeur de l'expression dans $v0 

# On met 2 à $v0 
	li $v0 2

# On empile 
	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)
	add $sp, $sp, -4	# Pour laisser de la place dans la pile
	la $a0, 0($s7)


# On depile 
	add $sp, $sp 4	# On remet la pile au bon endroit
	lw $v0, 0($sp)	# Pour stocker la valeur à $sp dans $v1

# On range $v0 à $a0 
	sw $v0, 0($a0)
	

#----------------------- a [ 2 ] := 8 

# Calcul de la valeur de l'expression dans $v0 

# On met 8 à $v0 
	li $v0 8

# On empile 
	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)
	add $sp, $sp, -4	# Pour laisser de la place dans la pile

# On met 2 à $v0 
	li $v0 2
	bltz $v0, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est < 0 
	bge $v0, 5, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est >= len(a) = 5 
	li $t0, -4
	mult $v0, $t0
	mflo $v0
	la $a0, -4($s7)
	add $a0, $a0 $v0

# On depile 
	add $sp, $sp 4	# On remet la pile au bon endroit
	lw $v0, 0($sp)	# Pour stocker la valeur à $sp dans $v1

# On range $v0 à $a0 
	sw $v0, 0($a0)
	

#----------------------- b [ 3 ] := a [ 2 ] 

# Calcul de la valeur de l'expression dans $v0 

# On met 2 à $v0 
	li $v0 2
	bltz $v0, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est < 0 
	bge $v0, 5, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est >= len(a) = 5 
	li $t0, -4
	mult $v0, $t0
	mflo $v0
	la $a0, -4($s7)
	add $a0, $a0 $v0
#-------- On range dans $a0 l'adresse cible : a [ 2 ] 
	lw $v0, ($a0)

# On empile 
	sw $v0, 0($sp)		# Pour mettre la valeur de $v0 à $sp (le haut de la pile)
	add $sp, $sp, -4	# Pour laisser de la place dans la pile

# On met 3 à $v0 
	li $v0 3
	bltz $v0, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 3 est < 0 
	bge $v0, 5, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 3 est >= len(b) = 5 
	li $t0, -4
	mult $v0, $t0
	mflo $v0
	la $a0, -24($s7)
	add $a0, $a0 $v0

# On depile 
	add $sp, $sp 4	# On remet la pile au bon endroit
	lw $v0, 0($sp)	# Pour stocker la valeur à $sp dans $v1

# On range $v0 à $a0 
	sw $v0, 0($a0)
	
# Affichage du saut de ligne 

# On met 2 à $v0 
	li $v0 2
	bltz $v0, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est < 0 
	bge $v0, 5, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 2 est >= len(a) = 5 
	li $t0, -4
	mult $v0, $t0
	mflo $v0
	la $a0, -4($s7)
	add $a0, $a0 $v0
	la $v0, 0($a0)
	lw $a0, 0($v0)
	li $v0, 1
	syscall

	li $v0, 4
	la $a0, newLine
	syscall
	
# Affichage du saut de ligne 

# On met 3 à $v0 
	li $v0 3
	bltz $v0, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 3 est < 0 
	bge $v0, 5, exceptionValeurHorsDomaine
#-------- On fait le test pour savoir si la valeur de 3 est >= len(b) = 5 
	li $t0, -4
	mult $v0, $t0
	mflo $v0
	la $a0, -24($s7)
	add $a0, $a0 $v0
	la $v0, 0($a0)
	lw $a0, 0($v0)
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

#----------------------------

# Fonctions pour gérer les exceptions 

# Si il y a un appel hors du domaine du tableau
 
exceptionValeurHorsDomaine:
	 move $t1, $v0
	 li $v0, 4

# On apelle le message 'hors domaine' 
	 la $a0, messageHorsDomaine

# Puis on l'affiche 
	 syscall


# On affiche la taille max du tableau 
	 la $a0, messageTailleMax
	 syscall

	 move $a0, $t0
	 li $v0, 1
	 syscall


# On affiche l'index désiré 
	 la $a0, messageIndex
	 li $v0, 4
	 syscall

	 move $v0, $t1


# Saut de ligne 

# On affiche $a0 
	 move $a0, $v0
	 li $v0, 1
	 syscall


# Affichage du saut de ligne "\n" 
	 li $v0, 4
	 la $a0, newLine
	 syscall
	 li $a0, 1
	 j exit
