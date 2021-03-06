package com.x5.template.filters;

import com.x5.template.Chunk;

public class OnEmptyFilter extends BasicFilter implements ChunkFilter {
    public String transformText(Chunk chunk, String text, FilterArgs arg) {
        String swapFor = null;
        String[] args = arg.getFilterArgs();
        if (args != null && args.length > 0) {
            swapFor = args[0];
        }
        if (swapFor == null) {
            return null;
        }
        return (text == null || text.trim().length() == 0) ? FilterArgs.magicBraces(chunk, swapFor) : text;
    }

    public String getFilterName() {
        return "onempty";
    }

    public String[] getFilterAliases() {
        return new String[]{"else"};
    }
}
