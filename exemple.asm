.text
	main:
	
	# variables
	# i : s7 - 0
	# j : s7 - 4
	# k : s7 - 8
	# l : s7 - 12




	# cr�ation des entiers

	move $s7, $sp
	add $sp, $sp, -16 # on r�serve 16 bits pour 4 entiers de 4 bits chacun
	
	

		
	# affectation de i et j
	
	li $v0, 81 # on stocke 81 dans i
	sw $v0, 0($s7)
	
	li $v0, 2 # on stocke 2 dans j
	sw $v0, -4($s7)




	# affichage de i et j
	
	li $v0, 1 # on pr�pare l'affichage des variables
	
	lw $a0, 0($s7) # on affiche i
	syscall
	
	lw $a0, -4($s7) # on affiche j
	syscall




	# affectation de k et l
	
	lw $v0, 0($s7) # on stocke la valeur de i dans v0
	
	sw $v0, -8($s7) # on met la valeur de i dans k
	sw $v0, -12($s7) # on met la valeur de i dans l




	# affichage de k et l
	
	li $v0, 1 # on pr�pare l'affichage des variables
	
	lw $a0, -8($s7) # on affiche k
	syscall
	
	lw $a0, -12($s7) # on affiche l
	syscall




	# condition si
	
	lw $s0, 0($s7) # on met i dans s0
	lw $s1, -8($s7) # on met k dans s1
	
	lw $s2, -12($s7) # on met l dans s2 pour pr�parer l'affectation
	
	beq $s0, $s1, si # on compare i et k
	
	li $s2, 180 # si i est diff�rent de k, on affecte 180 � l
	b finsi
	
	si:
	li $s2, 18 # si i est �gal � k, on affecte 18 � l
	
	finsi:
	sw $s2, -12($s7) # on copie finalement s2 dans la vraie variable l




	# boucle
	
	# � ce moment, la valeur de l est d�j� dans le registre s2 donc pas besoin de recopier � nouveau l dans s3
	
	debut_boucle:
	add $s0, $s2, -1 # on soustrait 1 � s2 (variable l) et on met le r�sultat dans s0
	blez $s0 sortie_boucle # on sort de la boucle si (l - 1) <= 0
	
	li $v0, 1 # on pr�pare l'affichage des variables
	move $a0, $s2 # on pr�pare l'affichage de l
	syscall # on affiche
	
	add $s2, $s2, -10 # on soustrait 10 � l
	
	b debut_boucle
	
	sortie_boucle:
	sw $s2, -12($s7) # on recopie finalement s2 dans la vraie variable l




	# boucle pour
	
	li $s0 1 # la valeur du registre s0 sera celle de la variable j (plus rapide), la variable j est ici une nouvelle variable diff�rente du j d�fini plus haut (nouveau bloc d'instruction)

	li $v0, 1 # on pr�pare l'affichage de k
	lw $a0, -8($s7) # on stocke la valeur de k dans a0 pour afficher k

	debut_pour:
	add $s1, $s0, -10 # on soustrait 10 � j et on met le r�sultat dans s1
	bgtz $s1, sortie_pour # on sort de la boucle si (j - 10) > 0

	syscall # on affiche k

	add $s0, $s0, 1 # on ajoute 1 � j

	b debut_pour

	sortie_pour: