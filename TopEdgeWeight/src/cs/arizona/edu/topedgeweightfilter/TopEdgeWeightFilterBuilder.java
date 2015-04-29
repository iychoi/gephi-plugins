package cs.arizona.edu.topedgeweightfilter;

import javax.swing.Icon;
import javax.swing.JPanel;
import org.gephi.filters.api.FilterLibrary;
import org.gephi.filters.spi.Category;
import org.gephi.filters.spi.Filter;
import org.gephi.filters.spi.FilterBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author iychoi
 */
@ServiceProvider(service = FilterBuilder.class)
public class TopEdgeWeightFilterBuilder implements FilterBuilder {

    @Override
    public Category getCategory() {
        return FilterLibrary.EDGE;
    }

    @Override
    public String getName() {
        return "Top Edge Weight";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Keep top N weighted edges";
    }

    @Override
    public Filter getFilter() {
        return new TopEdgeWeightFilter();
    }

    @Override
    public JPanel getPanel(Filter filter) {
        TopEdgeWeightFilterPanel panel = new TopEdgeWeightFilterPanel(); 
        panel.setup((TopEdgeWeightFilter) filter); 
        return panel; 
    }

    @Override
    public void destroy(Filter filter) {
    }
    
}
