//Nguyen Pham -s315303


import java.util.*;


public class ObligSBinTre<T> implements Beholder<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){ return "" + verdi;}

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public ObligSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi)
    {
        if (verdi == null)
            throw new NullPointerException("Ulovlig nullverdi!");

        Node<T> p = rot;
        Node<T> q = null;
        int cmp = 0;

        while (p != null) {
            q = p;
            cmp = comp.compare(verdi, p.verdi);
            p = cmp < 0 ? p.venstre : p.høyre;
        }

        p = new Node<>(verdi, q);

        if (q == null)
            rot = p;
        else if (cmp < 0)
            q.venstre = p;
        else
            q.høyre = p;

        antall++;
        endringer++;

        return true;

    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi)
    {
        if(verdi == null) return false;

        Node<T> p = rot, q = null;

        while(p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if(cmp < 0) { q = p; p = p.venstre;}
            else if(cmp > 0){ q = p; p = p.høyre;}
            else break;
        }
        if(p == null) return false;

        if(p.venstre == null || p.høyre == null)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;
            if(p == rot) rot = b;
            else if(p == q.venstre)
            { q.venstre = b;
                if( b != null) b.forelder = q;
            }
            else

            {
                q.høyre = b;
                if( b != null) b.forelder = q;
            }
        }
   else
        {
            Node<T> s = p, r = p.høyre;
            while(r.venstre != null)
            {
                s = r;
                r = r.venstre;
            }

            p.verdi = r.verdi;
            if(s != p)
            {
                s.venstre = r.høyre;
                if(r.høyre != null) r.høyre.forelder = s;
            }

            else
            {
                s.høyre = r.høyre;
                if(r.høyre != null) r.høyre.forelder = s;
            }
        }
        antall--;
        endringer++;
        return true;

    }

    public int fjernAlle(T verdi)
    {
        int i = 0;

        while(fjern(verdi)) i++;

        return i;

    }

    @Override
    public int antall()
    {
        return antall;
    }

    public int antall(T verdi)
    {

        if (verdi == null)
            return 0;

        Node<T> p = rot;
        int ant = 0;
        int cmp;

        while (p != null) {
            cmp = comp.compare(verdi, p.verdi);

            if (cmp < 0)
                p = p.venstre;
            else {
                if (cmp == 0)
                    ant++;

                p = p.høyre;
            }
        }

        return ant;




    }

    @Override
    public boolean tom()
    {
        return antall == 0;
    }

    @Override
    public void nullstill()
    {

        while(!tom())
        {
            if(rot.høyre == null)
            rot = rot.venstre;

        else
            {
                Node<T> p = rot.høyre, q = rot;
                while(p.høyre != null)
                {
                    q = p;
                    p = p.høyre;
                }
                q.høyre = p.venstre;
            }
            antall--;
            endringer++;
        }

    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {
        if (p.høyre != null) {
            p = p.høyre;

            while (p.venstre != null)
                p = p.venstre;
        }
        else {
            while (p.forelder != null && p == p.forelder.høyre) {
                p = p.forelder;
            }

            p = p.forelder;
        }

        return p;
    }

    @Override
    public String toString()
    {
        if (tom())
            return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = rot;

        while (p.venstre != null)
            p = p.venstre;

        while (p != null) {
            s.add(p.verdi.toString());

            p = nesteInorden(p);
        }

        return s.toString();
    }

    private static<T> Node<T> laveste(Node<T> p)
    {
        if (p == null)
        {
            return null;
        }

        if (p.venstre != null)
        {
            return laveste(p.venstre);
        }
        return p;
    }

    public String omvendtString()
    {

        StringBuilder s = new StringBuilder();
        s.append('[');
        Stack stakk = new Stack();
        Node p = laveste(rot);
        if(p != null)
        {
            stakk.push (p);
            while (nesteInorden(p) != null)
            {
                p = nesteInorden(p);
                stakk.push (p);
            }
        }

        while ( !stakk.empty() )
        {
            s.append( stakk.pop() );
            if(!stakk.empty())
                s.append(",").append(" ");
        }
        s.append(']');
        return s.toString();

    }

    public String høyreGren()
    {
        StringJoiner sj = new StringJoiner(", ", "[", "]");

        if(!tom())
        {
            Node<T> p = rot;
            sj.add(p.verdi.toString());

            while(p.høyre != null || p.venstre != null)
            {
                if(p.høyre != null) p = p.høyre;
           else p = p.venstre;
                sj.add(p.verdi.toString());
            }

        }
        return sj.toString();

    }

    public String lengstGren()
    {
        if (tom()) return "[]";

        ArrayDeque<Node<T>> kø = new ArrayDeque<>();
        kø.addFirst(rot);
        Node<T> p = rot;

        while (!kø.isEmpty())
        {
            p = kø.removeLast();
            if (p.høyre != null) kø.addFirst(p.høyre);
            if (p.venstre != null) kø.addFirst(p.venstre);
        }

        T verdi = p.verdi;
        p = rot;

        StringJoiner sj = new StringJoiner(", ", "[", "]");

        sj.add(p.verdi.toString());

        while(p.høyre != null || p.venstre != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if(cmp < 0) {  p = p.venstre;}
            else if(cmp > 0){ p = p.høyre;}
            else break;
            sj.add(p.verdi.toString());
        }

        return sj.toString();

    }

    public String[] grener()
    {
        if(tom()) return new String[0];

        String[] grener = new String[1];
        StringJoiner string;
        ArrayDeque<Node<T>> kø = new ArrayDeque();
        ArrayDeque<Node<T>> gren = new ArrayDeque();

        boolean tomliste = false;

        Node<T> p = rot;
        int i = 0;

        while(!tomliste)
        {
            string = new StringJoiner(", ", "[", "]");

            while(p.venstre != null || p.høyre != null)
            {
                if(p.venstre != null)
                {
                    if(p.høyre != null)
                    kø.add(p.høyre);
                    p = p.venstre;
                }
                else if(p.høyre != null)
                {
                    p = p.høyre;
                }
            }

            while(p != null)
            {
                gren.add(p);
                p = p.forelder;
            }
            while(!gren.isEmpty())

                string.add(gren.pollLast().toString());

            if(grener[grener.length -1] != null)

                grener = Arrays.copyOf(grener, grener.length + 1);

            grener[i++] = string.toString();

            if(!kø.isEmpty()) p = kø.pollLast();
             else tomliste = true;
        }
        return grener;

    }

    public String bladnodeverdier()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("[");
        Node p = rot;
        finnBladnode(p, stringbuilder);
        stringbuilder.append("]");
        return stringbuilder.toString();

    }

    private void finnBladnode(Node p, StringBuilder stringBuilder)
    {
        if (p == null) return;

        if(p.venstre == null && p.høyre == null)
        {
            if(!stringBuilder.toString().equals("["))
            {
                stringBuilder.append(",").append(" ").append(p);
            } else
            {
                stringBuilder.append(p);
            }
        }
        finnBladnode(p.venstre, stringBuilder);
        finnBladnode(p.høyre, stringBuilder);
    }


    public String postString()
    {
        StringJoiner sj = new StringJoiner(", ", "[", "]");

        if(!tom()) finnNode(rot, sj);

        return sj.toString();



    }
    private void finnNode(Node<T> p, StringJoiner sj)
    {
        if(p.venstre != null) finnNode(p.venstre, sj);
        if(p.høyre != null) finnNode(p.høyre, sj);
        if(p.venstre == null && p.høyre == null);

        sj.add(p.verdi.toString());
    }

    @Override
    public Iterator<T> iterator()
    {
        return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator()  // konstruktør
        {
            if(tom()) return;

            while(true)
            {
                if(p.venstre != null) p = p.venstre;
                else if (p.høyre != null) p = p.høyre;
            else break;
            }

        }

        @Override
        public boolean hasNext()
        {
            return p != null;  // Denne skal ikke endres!
        }

        @Override
        public T next()
        {
            if(!hasNext())
                throw new NoSuchElementException("ingen flere bladnoder.!");

            else if (endringer != iteratorendringer)
            {
                throw new ConcurrentModificationException("Endringer(" + endringer + ") != iteratorendringer(" + iteratorendringer + ")");
            }

            removeOK = true;

            q = p;
            T temp = p.verdi;
            while(hasNext())
            {
                p = nesteInorden(p);
                if(p == null)
                    return temp;

                if(p.venstre == null && p.høyre == null)
                return temp;
            }
            return temp;

        }

        @Override
        public void remove()
        {
            if(!removeOK)
                throw new IllegalStateException("Ulovlig kall!");

            removeOK = false;

            if(q.forelder == null)
            {
                rot = null;
            }

            else
            {
                if(q.forelder.venstre == q)
                    q.forelder.venstre = null;

                else
                    q.forelder.høyre = null;
            }
            antall--;
            endringer++;
            iteratorendringer++;
            removeOK = false;

        }

    } // BladnodeIterator

} // ObligSBinTre
