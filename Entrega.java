import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {
  static final String[] NOMS = {"Maria Victoria Barcos Silva","Elián Freire Bauzá","Jaime Cañellas Sanz"};

  /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
   */
  static class Tema1 {
    /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
     */
    static final char CONJ = '∧';
    static final char DISJ = '∨';
    static final char IMPL = '→';
    static final char NAND = '.';

    static int exercici1(char[] ops, int[] vars) {
      // Cercam el nombre de variables diferents cercant el maxim a la llista
      int nVars = 0;
      for (int i : vars) {
        if (i > nVars)
          nVars = i;
      }
      nVars++;
      // Generam les combinacions de les variables com si fos una taula de veritat
      int[][] combinacions = new int[nVars][((int) Math.pow(2, nVars))];
      int contador;
      int value;
      for (int i = 0; i < combinacions.length; i++) {
        contador = 0;
        value = 0;
        for (int j = 0; j < combinacions[i].length; j++) {
          if (contador == Math.pow(2, i)) {
            value = ((value + 1) % 2);
            contador = 0;
          }
          combinacions[i][j] = value;
          contador++;
        }

      }
      // feim les operacions
      int[] res = combinacions[vars[0]];
      contador = 1;
      for (char i : ops) {
        switch (i) {
          case CONJ -> {
            res = conjuncio(res, combinacions[vars[contador]]);
          }
          case DISJ -> {
            res = disjuncio(res, combinacions[vars[contador]]);
          }
          case IMPL -> {
            res = implicacio(res, combinacions[vars[contador]]);
          }
          case NAND -> {
            res = nand(res, combinacions[vars[contador]]);
          }
        }
        contador++;
      }
      // ara tenim a res el resultat de la taula de veritat
      boolean tautologia = true;
      boolean contradiccio = true;
      for (int i : res) {
        if (contradiccio && i == 1) {
          contradiccio = false;
        } else if (tautologia && i == 0) {
          tautologia = false;
        } else {
          break;
        }
      }
      if (tautologia) {
        return 1;
      } else if (contradiccio) {
        return 0;
      } else {
        return -1;
      }
    }

    /*
     * Aquest metode realitza la operació AND amb dues cadenes de bits
     */
    static int[] conjuncio(int[] var1, int[] var2) {
      int[] res = new int[var1.length];
      for (int i = 0; i < Math.min(var1.length, var2.length); i++) {
        if (var1[i] == 1 && var2[i] == 1) {
          res[i] = 1;
        } else {
          res[i] = 0;
        }
      }
      return res;
    }

    /*
     * Aquest metode realitza la operació OR amb dues cadenes de bits
     */
    static int[] disjuncio(int[] var1, int[] var2) {
      int[] res = new int[var1.length];
      for (int i = 0; i < Math.min(var1.length, var2.length); i++) {
        if (var1[i] == 1 || var2[i] == 1) {
          res[i] = 1;
        } else {
          res[i] = 0;
        }
      }
      return res;
    }

    /*
     * Aquest metode realitza la operació IMPL amb dues cadenes de bits
     */
    static int[] implicacio(int[] var1, int[] var2) {
      int[] res = new int[var1.length];
      for (int i = 0; i < Math.min(var1.length, var2.length); i++) {
        if (var1[i] == 1 && var2[i] == 0) {
          res[i] = 0;
        } else {
          res[i] = 1;
        }
      }
      return res;
    }

    /*
     * Aquest metode realitza la operació NAND amb dues cadenes de bits
     */
    static int[] nand(int[] var1, int[] var2) {
      int[] res = new int[var1.length];
      for (int i = 0; i < Math.min(var1.length, var2.length); i++) {
        if (var1[i] == 1 && var2[i] == 1) {
          res[i] = 0;
        } else {
          res[i] = 1;
        }
      }
      return res;
    }

    /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
     */
    static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
      int elementsP = 0;
      int elementsQ = 0;
      for (int i : universe) {
        if (p.test(i))
          elementsP++;
        if (q.test(i))
          elementsQ++;
      }
      return (elementsP == universe.length) == (elementsQ == 1);
    }

    static void tests() {
      // Exercici 1
      // Taules de veritat

      // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
      test(1, 1, 1, () -> exercici1(new char[] { IMPL, DISJ, DISJ }, new int[] { 0, 2, 1, 0 }) == 1);

      // Contradicció: (p0 . p0) ∧ p0
      test(1, 1, 2, () -> exercici1(new char[] { NAND, CONJ }, new int[] { 0, 0, 0 }) == 0);

      // Exercici 2
      // Equivalència

      test(1, 2, 1, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x == 0, (x) -> x == 0);
      });

      test(1, 2, 2, () -> {
        return exercici2(new int[] { 1, 2, 3 }, (x) -> x >= 1, (x) -> x % 2 == 0);
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
   */
  static class Tema2 {
    /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
     */
    static int exercici1(int[] a) {
      // S(n,k) = k·S(n-1,k) + S(n-1,k-1)
      // Posibles particions = S(n,n) + S(n,n-1) + S(n,n-2) ... +S(n,1) +S(n,0)
      // S(n,0)=0
      // S(n,1)=1
      // S(n,n)=1
      int n = a.length;
      int res = 0;
      for (int i = n; i > 0; i--) {
        res += nStirling(n, i);
      }
      return res;
    }

    /*
     * Metode que calcula el nombre de particions de un conjunt de n elements en
     * conjunts de k elements
     */
    static int nStirling(int n, int k) {
      if (k == 1 || k == n) {
        return 1;
      } else {
        return k * nStirling(n - 1, k) + nStirling(n - 1, k - 1);
      }
    }

    /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
     */
    static int exercici2(int[] a, int[][] rel) {
      // inicialitzam una matriu d'adjacencia amb totes les posicions a false
      boolean[][] matAdjecencia = new boolean[a.length][a.length];
      // posam a true matriu[i][j] en base a les relacions iRj en rel
      for (int[] r : rel) {
        matAdjecencia[r[0] - a[0]][r[1] - a[0]] = true;
      }
      // Aplicam l'algoritme Floid-Warsall per obtenir la clausura transitiva
      for (int i = 0; i < matAdjecencia.length; i++) {
        for (int j = 0; j < matAdjecencia.length; j++) {
          for (int k = 0; k < matAdjecencia.length; k++) {
            if (matAdjecencia[i][j] && matAdjecencia[j][k]) {
              matAdjecencia[i][k] = true;
            }
          }
        }
      }
      // Posam les diagonals de la matriu per complir la reflexivitat
      for (int i = 0; i < matAdjecencia.length; i++) {
        matAdjecencia[i][i] = true;
      }
      // comprovam que no hi hagi simetria en relacions nRm on n!=m
      for (int i = 1; i < matAdjecencia.length; i++) {
        for (int j = 0; j < i; j++) {
          if (matAdjecencia[i][j] && matAdjecencia[j][i]) {
            return -1;
          }
        }
      }
      // La matriu es valida, contam el nombre de relacions
      int cardinal = 0;
      for (boolean[] fila : matAdjecencia) {
        for (boolean columna : fila) {
          if (columna) {
            cardinal++;
          }
        }
      }
      return cardinal;
    }

    /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
     */
    static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {
      Integer res = null;
      boolean valid = true;
      if (op) {
        // Cercam el suprem
        for (int[] r : rel) {
          if (x[0] == r[0]) {
            // Significa que existeix {x[0],r[1]} a la relació
            for (int i = 1; valid && i < x.length; i++) {
              valid = false;
              // Comprovam la existencia de {x[i],r[1]} a la relació per totes les x
              for (int j = 0; !valid && j < rel.length; j++) {
                if (rel[j][0] == x[i] && rel[j][1] == r[1]) {
                  valid = true;
                }
              }
            }
            if (valid) {
              res = r[1];
              break; // Sortim del bucle perque al estar ordenades les relacions el suprem sera el
                     // primer en apareixer que compli la condició
            }
          }
        }
      } else {
        // Cercam l'infim
        for (int[] r : rel) {
          if (r[1] == x[0]) {
            // Significa que existeix {r[0],x[0]} a la relació
            for (int i = 1; valid && i < x.length; i++) {
              valid = false;
              // Comprovam la existencia de {r[0],x[i]} a la relació per totes les x
              for (int j = 0; !valid && j < rel.length; j++) {
                if (rel[j][0] == r[0] && rel[j][1] == x[i]) {
                  valid = true;
                }
              }
            }
            if (valid)
              res = r[0]; // Seguim a la repetició perque al estar ordenades les relacions el infim sera
                          // el darrer element en complir les condicións
          }
        }
      }
      return res;
    }

    /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
     */
    static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {
      boolean injectiva, sobrejectiva;
      injectiva = sobrejectiva = true;

      if (a.length < b.length) {
        sobrejectiva = false;
      } else if (b.length < a.length) {
        injectiva = false;
      }

      if (injectiva) { // Comprovam la injectivitat
        for (int i = 0; injectiva && i < a.length; i++) {
          for (int j = i + 1; injectiva && j < a.length; j++) {
            if (f.apply(a[i]) == f.apply(a[j])) {
              injectiva = false;
            }
          }
        }
      }
      boolean trobat;
      if (sobrejectiva) { // Comprovam si realment es sobrejectiva
        for (int e : b) {
          trobat = false;
          for (int v : a) {
            if (f.apply(v) == e) {
              trobat = true;
              break;
            }
          }
          if (!trobat) {
            sobrejectiva = false;
            break;
          }
        }
      }

      if (!injectiva && !sobrejectiva) {
        return null;
      }

      // El seguent algoritme forme les inverses independenment siguin isomorfs, per
      // la esquerra o dreta
      int[][] graf = new int[b.length][2];

      int index = -1;
      for (int e : b) {
        trobat = false;
        index++;
        for (int v : a) {
          if (f.apply(v) == e) {
            graf[index] = new int[] { e, v };
            trobat = true;
            break;
          }
        }
        if (!trobat) {
          graf[index] = graf[index - 1];
        }
      }
      return graf;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {
      // Exercici 1
      // Nombre de particions

      test(2, 1, 1, () -> exercici1(new int[] { 1 }) == 1);
      test(2, 1, 2, () -> exercici1(new int[] { 1, 2, 3 }) == 5);

      // Exercici 2
      // Clausura d'ordre parcial

      final int[] INT02 = { 0, 1, 2 };

      test(2, 2, 1, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 2} }) == 6);
      test(2, 2, 2, () -> exercici2(INT02, new int[][] { {0, 1}, {1, 0}, {1, 2} }) == -1);

      // Exercici 3
      // Ínfims i suprems

      final int[] INT15 = { 1, 2, 3, 4, 5 };
      final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
      final Integer ONE = 1;

      test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[] { 2, 3 }, false)));
      test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[] { 2, 3 }, true) == null);

      // Exercici 4
      // Inverses

      final int[] INT05 = { 0, 1, 2, 3, 4, 5 };

      test(2, 4, 1, () -> {
        var inv = exercici4(INT05, INT02, (x) -> x/2);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT02.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1]/2 != i)
            return false;
        }

        return true;
      });

      test(2, 4, 2, () -> {
        var inv = exercici4(INT02, INT05, (x) -> x);

        if (inv == null)
          return false;

        inv = lexSorted(inv);

        if (inv.length != INT05.length)
          return false;

        for (int i = 0; i < INT02.length; i++) {
          if (inv[i][0] != i || inv[i][1] != i)
            return false;
        }

        return true;
      });
    }

    /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
     */
    static int[][] lexSorted(int[][] arr) {
      if (arr == null)
        return null;

      var arr2 = Arrays.copyOf(arr, arr.length);
      Arrays.sort(arr2, Arrays::compare);
      return arr2;
    }

    /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
     */
    static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
      var rel = new ArrayList<int[]>();

      for (int a : as) {
        for (int b : bs) {
          if (pred.test(a, b)) {
            rel.add(new int[] { a, b });
          }
        }
      }

      return rel.toArray(new int[][] {});
    }

    // Especialització de generateRel per as = bs
    static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
      return generateRel(as, as, pred);
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
   */
  static class Tema3 {
    /*
     * Determinau si el graf `g` (no dirigit) té cicles.
     */
    static boolean exercici1(int[][] g) {
      boolean[] explorats = new boolean[g.length];
      for (int i = 0; i < g.length; i++) {
        if (exploreExercici1(g, i, explorats, i)) {
          return true;
        }
      }
      return false;
    }

    static boolean exploreExercici1(int[][] g, int v, boolean[] explorats, int anterior) {
      if (explorats[v]) {
        return true; // Ha trobat un cami a un graf ja explorat diferent a l'original, te un cicle
      }
      explorats[v] = true;
      for (int vNext : g[v]) {
        if (vNext != anterior) {
          if (exploreExercici1(g, vNext, explorats, v)) {
            return true;
          }
        }
      }
      return false;
    }

    /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
     */
    static boolean exercici2(int[][] g1, int[][] g2) {
      if (g1.length != g2.length) { // Si no tenen el mateix nombre de vertexos no poden ser isomorfs
        return false;
      }
      // Cream llistes dels graus dels vertexos
      int[] grausG1 = new int[g1.length];
      for (int i = 0; i < g1.length; i++) {
        grausG1[i] = g1[i].length;
      }
      int[] grausG2 = new int[g2.length];
      for (int i = 0; i < g2.length; i++) {
        grausG2[i] = g2[i].length;
      }
      // Els ordenam
      Arrays.sort(grausG1);
      Arrays.sort(grausG2);
      // Comprovam que siguin iguals
      if (Arrays.equals(grausG1, grausG2)) {
        // Aplicam totes les posibles permutacions i comprovam si aplicant qualque a g1
        // formam g2
        int[] perm = new int[g1.length];
        for (int i = 0; i < perm.length; i++) {
          perm[i] = i;
        }
        return comprovarPermutacions(perm, 0, g1, g2);
      } else {
        return false;
      }
    }

    /*
     * Métode que aplicant permutacions tal que renombram els nodes de g1 miram si
     * amb qualquna transformacio obtenim g2
     */
    static boolean comprovarPermutacions(int[] perm, int indx, int[][] g1, int[][] g2) {
      if (indx == perm.length - 1) { // Permutació completa
        // Comprovam si aplicant la permutacio obtinguda a g1 obtenim g2
        int[][] fg1 = new int[g1.length][];
        for (int i = 0; i < g1.length; i++) {// Cream una copia per no modificar g1
          fg1[perm[i]] = g1[i].clone();
        }
        // Canviam els veinats dels nodes segons perm
        for (int i = 0; i < fg1.length; i++) {
          for (int j = 0; j < fg1[i].length; j++) {
            fg1[i][j] = perm[fg1[i][j]];
          }
          Arrays.sort(fg1[i]);
        }
        // Comprovam que ambdues llistes d'adjacencia siguin iguals
        for (int i = 0; i < fg1.length; i++) {
          if (!Arrays.equals(fg1[i], g2[i])) {
            return false;
          }
        }
        return true;
      } else {
        // Seguim generant les permutacions per backtracking
        int aux;
        for (int i = indx; i < perm.length; i++) {
          // Feim un swap
          aux = perm[indx];
          perm[indx] = perm[i];
          perm[i] = aux;
          if (comprovarPermutacions(perm, indx + 1, g1, g2)) {
            return true; // N'ha trobada una permutacio tal que f(g1) = g2
          }
          // Desfem el canvi
          aux = perm[indx];
          perm[indx] = perm[i];
          perm[i] = aux;
        }
        return false;
      }
    }

    /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
     */
    static int[] exercici3(int[][] g, int r) {
      boolean[] explorats = new boolean[g.length];
      List<Integer> postOrdre = new ArrayList<Integer>();
      if (!exploreExercici3(g, r, r, explorats, postOrdre)) {
        return null; // El graf te cicles, no es un abre
      } else {
        // Comprovam que el graf sigui connex, tots els vertexos han de estar explorats
        for (boolean explorat : explorats) {
          if (!explorat) {
            return null;
          }
        }
        // Ficam els elements de la llista postOrdre dins un int[]
        int[] res = new int[g.length];
        for (int i = 0; i < postOrdre.size(); i++) {
          res[i] = postOrdre.get(i);
        }
        return res;
      }
    }

    static boolean exploreExercici3(int[][] g, int v, int anterior, boolean[] explorats, List<Integer> postordre) {
      if (explorats[v]) {
        return false; // Ha trobat un cami a un graf ja explorat diferent a l'original, te un cicle
      }
      explorats[v] = true;
      for (int vNext : g[v]) {
        if (vNext != anterior) {
          if (!exploreExercici3(g, vNext, v, explorats, postordre)) {
            return false; // Ha trobat un cicle en la seva recursivitat
          }
        }
      }
      postordre.add(v);
      return true; // No te cicles
    }

    /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
     */
    static int exercici4(char[][] mapa) {
      boolean[][] explorat = new boolean[mapa.length][mapa[0].length]; // Feim un array de booleans que marcara si una
                                                                       // casella ja ha estat explorada per no repetir
                                                                       // la cerca
      List<int[]> aExplorar = new ArrayList<int[]>(); // Empream una arraylist que emprarem com a cua per aplicar la
                                                      // cerca BFS
      boolean trobat = false;
      // cercam la posicio d'origen
      for (int fila = 0; !trobat && fila < mapa.length; fila++) {
        for (int columna = 0; !trobat && columna < mapa[0].length; columna++) {
          if (mapa[fila][columna] == 'O') {
            aExplorar.add(new int[] { fila, columna }); // Ficam l'origen, el primer node a explorar
            explorat[fila][columna] = true;
            trobat = true; // sortim del bucle
          }
        }
      }
      int pases = 0;
      while (!aExplorar.isEmpty()) {
        int tam = aExplorar.size();
        for (int i = 0; i < tam; i++) {
          int[] node = aExplorar.removeFirst(); // Feim el que seria un pop de una cua
          if (mapa[node[0]][node[1]] == 'D') {// Comprovam si trobam el desti
            return pases; // Amb la cerca en amplada ens aseguram de que es troba amb el minim de pases
          }

          // Ficam els adjacents, comprovam que no s'hagin explorat abans, no siguin '#'
          // ni surtin del mapa
          if (node[0] != mapa.length - 1 && !explorat[node[0] + 1][node[1]] && mapa[node[0] + 1][node[1]] != '#') {
            // Miram el de abaix
            explorat[node[0] + 1][node[1]] = true;
            aExplorar.add(new int[] { node[0] + 1, node[1] });
          }
          if (node[0] != 0 && !explorat[node[0] - 1][node[1]] && mapa[node[0] - 1][node[1]] != '#') {
            // Miram el de adalt
            explorat[node[0] - 1][node[1]] = true;
            aExplorar.add(new int[] { node[0] - 1, node[1] });
          }
          if (node[1] != mapa[0].length - 1 && !explorat[node[0]][node[1] + 1] && mapa[node[0]][node[1] + 1] != '#') {
            // Miram el de la dreta
            explorat[node[0]][node[1] + 1] = true;
            aExplorar.add(new int[] { node[0], node[1] + 1 });
          }
          if (node[1] != 0 && !explorat[node[0]][node[1] - 1] && mapa[node[0]][node[1] - 1] != '#') {
            // Miram el de la esquerra
            explorat[node[0]][node[1] - 1] = true;
            aExplorar.add(new int[] { node[0], node[1] - 1 });
          }
        }
        pases++;
      }
      return -1;
    }

    /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
     */
    static void tests() {

      final int[][] D2 = { {}, {} };
      final int[][] C3 = { {1, 2}, {0, 2}, {0, 1} };

      final int[][] T1 = { {1, 2}, {0}, {0} };
      final int[][] T2 = { {1}, {0, 2}, {1} };

      // Exercici 1
      // G té cicles?

      test(3, 1, 1, () -> !exercici1(D2));
      test(3, 1, 2, () -> exercici1(C3));

      // Exercici 2
      // Isomorfisme de grafs

      test(3, 2, 1, () -> exercici2(T1, T2));
      test(3, 2, 2, () -> !exercici2(T1, C3));

      // Exercici 3
      // Postordre

      test(3, 3, 1, () -> exercici3(C3, 1) == null);
      test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[] { 1, 2, 0 }));

      // Exercici 4
      // Laberint

      test(3, 4, 1, () -> {
        return -1 == exercici4(new char[][] {
            " #O".toCharArray(),
            "D# ".toCharArray(),
            " # ".toCharArray(),
        });
      });

      test(3, 4, 2, () -> {
        return 8 == exercici4(new char[][] {
            "###D".toCharArray(),
            "O # ".toCharArray(),
            " ## ".toCharArray(),
            "    ".toCharArray(),
        });
      });
    }
  }

  /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
   */
  static class Tema4 {
    /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
     */
    static int[] exercici1(String msg, int n, int e) {
      byte[] seq = msg.getBytes();
      int[] cod = new int[msg.length() / 2];
      for (int i = 0; i < cod.length; i++) {
        cod[i] = (int) seq[i * 2] * 128;
        cod[i] += (int) seq[(i * 2) + 1];
      }
      for (int i = 0; i < cod.length; i++) {
        cod[i] = mod_exp_by_squaring(cod[i], e, n);
      }
      return cod;
    }

    // Métode que eleva passa per pasa a l'exponent evitant overflow mitjançant
    // moduls intermitjos
    static int mod_exp_by_squaring(int x, int n, int mod) {
      if (n == 0) {
        return 1;
      } else if (n % 2 == 0) {
        return mod_exp_by_squaring((x * x) % mod, n / 2, mod);
      } else {
        return (x * mod_exp_by_squaring((x * x) % mod, (n - 1) / 2, mod)) % mod;
      }
    }
    
    /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     */
    static String exercici2(int[] m, int n, int e) {
      int p = 2;
      while (n % p != 0) { // Factoritzam n per aconseguir p i q
        p++;
      }
      int q = n / p;
      int phiEuler = (p - 1) * (q - 1);
      // Aplicam la identitat de bezout amb e i la phi de Euler de n per aconseguir la
      // inversa
      int d = inversa(e, phiEuler);
      // Elevam en d modul n cada bloc de codi
      for (int i = 0; i < m.length; i++) {
        m[i] = mod_exp_by_squaring(m[i], d, n); // empream el mateix metode que al exercici anterior per elevar nombres
                                                // grossos
      }
      String res = "";
      // Per cada bloc de codi extreim dos caracters
      for (int i = 0; i < m.length; i++) {
        int c0 = m[i] % 128; // Segon element del bloc
        int c1 = ((m[i] - c0) / 128) % 128; // primer element del bloc
        res += (char) c1;
        res += (char) c0;
      }
      return res;
    }

    /*
     * Metode que retorna la inversa de n modul mod, es necesari que n i mod siguin
     * coprimers
     */
    static int inversa(int n, int mod) {
      int modul = mod;
      int x_ant = 1;
      int x = 0;
      int aux;
      while (n % mod != 0) {
        // Calculam la x seguent
        aux = x_ant - n / mod * x;
        x_ant = x;
        x = aux;

        // Obtenenim els valors de la seguent divisió
        aux = mod;
        mod = n % mod;
        n = aux;
      }
      if (x < 0)
        x = x + modul; // si es negatiu cercam el valor positiu de la classe
      return x;
    }

    static void tests() {
      // Exercici 1
      // Codificar i encriptar
      test(4, 1, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = exercici1("Patata", n, e);
        return Arrays.equals(encr, new int[] { 4907, 4785, 4785 });
      });

      // Exercici 2
      // Desencriptar i decodificar
      test(4, 2, 1, () -> {
        var n = 2*8209;
        var e = 5;

        var encr = new int[] { 4907, 4785, 4785 };
        var decr = exercici2(encr, n, e);
        return "Patata".equals(decr);
      });
    }
  }

  /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
   */
  public static void main(String[] args) {
    System.out.println("---- Tema 1 ----");
    Tema1.tests();
    System.out.println("---- Tema 2 ----");
    Tema2.tests();
    System.out.println("---- Tema 3 ----");
    Tema3.tests();
    System.out.println("---- Tema 4 ----");
    Tema4.tests();
  }

  // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
  static void test(int tema, int exercici, int test, BooleanSupplier p) {
    try {
      if (p.getAsBoolean())
        System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
      else
        System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
    } catch (Exception e) {
      if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
        System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
      } else {
        System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
        e.printStackTrace();
      }
    }
  }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
