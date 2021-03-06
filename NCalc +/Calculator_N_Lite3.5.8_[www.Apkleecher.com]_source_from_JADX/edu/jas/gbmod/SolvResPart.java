package edu.jas.gbmod;

import edu.jas.poly.ModuleList;
import edu.jas.structure.RingElem;
import java.io.Serializable;

/* compiled from: SolvableSyzygyAbstract */
class SolvResPart<C extends RingElem<C>> implements Serializable {
    public final ModuleList<C> GB;
    public final ModuleList<C> module;
    public final ModuleList<C> syzygy;

    public SolvResPart(ModuleList<C> m, ModuleList<C> g, ModuleList<C> z) {
        this.module = m;
        this.GB = g;
        this.syzygy = z;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("SolvResPart(\n");
        s.append("module = " + this.module);
        s.append("\n GB = " + this.GB);
        s.append("\n syzygy = " + this.syzygy);
        s.append(")");
        return s.toString();
    }
}
