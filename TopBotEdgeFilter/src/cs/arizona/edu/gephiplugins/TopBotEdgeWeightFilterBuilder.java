/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.arizona.edu.gephiplugins;

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
public class TopBotEdgeWeightFilterBuilder implements FilterBuilder {

    @Override
    public Category getCategory() {
        return FilterLibrary.EDGE;
    }

    @Override
    public String getName() {
        return "Top Bottom Edge Filter";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Filters Top N or Bottom N edges by their weight";
    }

    @Override
    public Filter getFilter() {
        return new TopBotEdgeWeightFilter();
    }

    @Override
    public JPanel getPanel(Filter filter) {
        //Create the panel 
        TopBotEdgeWeightFilterPanel panel = new TopBotEdgeWeightFilterPanel(); 
        panel.setup((TopBotEdgeWeightFilter) filter); 
        return panel; 

    }

    @Override
    public void destroy(Filter filter) {
    }
    
}
