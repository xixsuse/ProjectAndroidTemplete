package edu.jas.gb;

import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.GenSolvablePolynomial;
import edu.jas.poly.PolynomialList;
import edu.jas.structure.RingElem;
import edu.jas.util.Terminator;
import edu.jas.util.ThreadPool;
import io.github.kexanie.library.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;

public class SolvableGroebnerBaseParallel<C extends RingElem<C>> extends SolvableGroebnerBaseAbstract<C> {
    private static final Logger logger;
    protected final transient ThreadPool pool;
    protected final int threads;

    static {
        logger = Logger.getLogger(SolvableGroebnerBaseParallel.class);
    }

    public SolvableGroebnerBaseParallel() {
        this(2);
    }

    public SolvableGroebnerBaseParallel(int threads) {
        this(threads, new ThreadPool(threads));
    }

    public SolvableGroebnerBaseParallel(int threads, ThreadPool pool) {
        this(threads, pool, new SolvableReductionPar());
    }

    public SolvableGroebnerBaseParallel(int threads, SolvableReduction<C> sred) {
        this(threads, new ThreadPool(threads), (SolvableReduction) sred);
    }

    public SolvableGroebnerBaseParallel(int threads, PairList<C> pl) {
        this(threads, new ThreadPool(threads), new SolvableReductionPar(), pl);
    }

    public SolvableGroebnerBaseParallel(int threads, SolvableReduction<C> sred, PairList<C> pl) {
        this(threads, new ThreadPool(threads), sred, pl);
    }

    public SolvableGroebnerBaseParallel(int threads, ThreadPool pool, SolvableReduction<C> sred) {
        this(threads, pool, sred, new OrderedPairlist());
    }

    public SolvableGroebnerBaseParallel(int threads, ThreadPool pool, SolvableReduction<C> sred, PairList<C> pl) {
        super(sred, pl);
        if (!(sred instanceof SolvableReductionPar)) {
            logger.warn("parallel GB should use parallel aware reduction");
        }
        if (threads < 1) {
            threads = 1;
        }
        this.threads = threads;
        this.pool = pool;
    }

    public void terminate() {
        if (this.pool != null) {
            this.pool.terminate();
        }
    }

    public List<GenSolvablePolynomial<C>> leftGB(int modv, List<GenSolvablePolynomial<C>> F) {
        List<GenSolvablePolynomial<C>> G = new ArrayList();
        if (F == null) {
            return G;
        }
        PairList<C> pairlist = null;
        int l = F.size();
        ListIterator<GenSolvablePolynomial<C>> it = F.listIterator();
        while (it.hasNext()) {
            GenSolvablePolynomial<C> p = (GenSolvablePolynomial) it.next();
            if (p.length() > 0) {
                GenPolynomial p2 = p.monic();
                if (p2.isONE()) {
                    G.clear();
                    G.add(p2);
                    return G;
                }
                G.add(p2);
                if (pairlist == null) {
                    pairlist = this.strategy.create(modv, p2.ring);
                    if (!p2.ring.coFac.isField()) {
                        throw new IllegalArgumentException("coefficients not from a field");
                    }
                }
                pairlist.put(p2);
            } else {
                l--;
            }
        }
        if (l <= 1) {
            return G;
        }
        Terminator fin = new Terminator(this.threads);
        for (int i = 0; i < this.threads; i++) {
            this.pool.addJob(new LeftSolvableReducer(fin, G, pairlist));
        }
        fin.waitDone();
        logger.debug("#parallel list = " + G.size());
        G = leftMinimalGB(G);
        logger.info(BuildConfig.FLAVOR + pairlist);
        return G;
    }

    public List<GenSolvablePolynomial<C>> leftMinimalGB(List<GenSolvablePolynomial<C>> Fp) {
        ArrayList<GenSolvablePolynomial<C>> G = new ArrayList(Fp.size());
        ListIterator<GenSolvablePolynomial<C>> it = Fp.listIterator();
        while (it.hasNext()) {
            GenSolvablePolynomial<C> a = (GenSolvablePolynomial) it.next();
            if (a.length() != 0) {
                G.add(a);
            }
        }
        if (G.size() <= 1) {
            return G;
        }
        ArrayList<GenSolvablePolynomial<C>> F = new ArrayList(G.size());
        while (G.size() > 0) {
            a = (GenSolvablePolynomial) G.remove(0);
            ExpVector e = a.leadingExpVector();
            it = G.listIterator();
            boolean mt = false;
            while (it.hasNext() && !mt) {
                mt = e.multipleOf(((GenSolvablePolynomial) it.next()).leadingExpVector());
            }
            it = F.listIterator();
            while (it.hasNext() && !mt) {
                mt = e.multipleOf(((GenSolvablePolynomial) it.next()).leadingExpVector());
            }
            if (!mt) {
                F.add(a);
            }
        }
        G = F;
        if (G.size() <= 1) {
            return G;
        }
        SolvableMiReducer[] mirs = (SolvableMiReducer[]) new SolvableMiReducer[G.size()];
        int i = 0;
        F = new ArrayList(G.size());
        while (G.size() > 0) {
            a = (GenSolvablePolynomial) G.remove(0);
            List<GenSolvablePolynomial<C>> R = new ArrayList(G.size() + F.size());
            R.addAll(G);
            R.addAll(F);
            mirs[i] = new SolvableMiReducer(R, a);
            this.pool.addJob(mirs[i]);
            i++;
            F.add(a);
        }
        List<GenSolvablePolynomial<C>> F2 = new ArrayList(F.size());
        for (SolvableMiReducer nf : mirs) {
            F2.add(nf.getNF());
        }
        return F2;
    }

    public SolvableExtendedGB<C> extLeftGB(int modv, List<GenSolvablePolynomial<C>> list) {
        throw new UnsupportedOperationException("parallel extLeftGB not implemented");
    }

    public List<GenSolvablePolynomial<C>> twosidedGB(int modv, List<GenSolvablePolynomial<C>> Fp) {
        if (Fp == null || Fp.size() == 0) {
            return new ArrayList();
        }
        int i;
        List<GenSolvablePolynomial<C>> X = PolynomialList.castToSolvableList(((GenSolvablePolynomial) Fp.get(0)).ring.generators(modv));
        logger.info("right multipliers = " + X);
        List F = new ArrayList(Fp.size() * (X.size() + 1));
        F.addAll(Fp);
        for (i = 0; i < F.size(); i++) {
            GenSolvablePolynomial<C> p = (GenSolvablePolynomial) F.get(i);
            for (int j = 0; j < X.size(); j++) {
                GenSolvablePolynomial<C> x = (GenSolvablePolynomial) X.get(j);
                if (!x.isONE()) {
                    GenSolvablePolynomial<C> q = this.sred.leftNormalform(F, p.multiply((GenSolvablePolynomial) x));
                    if (!q.isZERO()) {
                        F.add(q);
                    }
                }
            }
        }
        List<GenSolvablePolynomial<C>> G = new ArrayList();
        PairList<C> pairlist = null;
        int l = F.size();
        ListIterator<GenSolvablePolynomial<C>> it = F.listIterator();
        while (it.hasNext()) {
            p = (GenSolvablePolynomial) it.next();
            if (p.length() > 0) {
                GenPolynomial p2 = p.monic();
                if (p2.isONE()) {
                    G.clear();
                    G.add(p2);
                    return G;
                }
                G.add(p2);
                if (pairlist == null) {
                    pairlist = this.strategy.create(modv, p2.ring);
                    if (!p2.ring.coFac.isField()) {
                        throw new IllegalArgumentException("coefficients not from a field");
                    }
                }
                pairlist.put(p2);
            } else {
                l--;
            }
        }
        if (l <= 1) {
            return G;
        }
        Terminator fin = new Terminator(this.threads);
        for (i = 0; i < this.threads; i++) {
            this.pool.addJob(new TwosidedSolvableReducer(fin, modv, X, G, pairlist));
        }
        fin.waitDone();
        logger.debug("#parallel list = " + G.size());
        G = leftMinimalGB(G);
        logger.info(BuildConfig.FLAVOR + pairlist);
        return G;
    }
}
