/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cs.arizona.edu.gephiplugins;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import org.gephi.filters.spi.EdgeFilter;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author iychoi
 */
public class TopBotEdgeWeightFilter implements EdgeFilter {

    private FilterProperty[] filterProperties; 
    private Integer bot = 0; 
    private Integer top = 0; 
    //Flag 
    private HashSet<Edge> filteredEdges;

    
    public TopBotEdgeWeightFilter() {
    }
    
    @Override
    public String getName() {
        return "Top Bottom Edge Filter";
    }

    @Override
    public FilterProperty[] getProperties() {
        if (this.filterProperties == null) {
            this.filterProperties = new FilterProperty[0];
            try {
                this.filterProperties = new FilterProperty[]{
                    FilterProperty.createProperty(this, Integer.class, "top"),
                    FilterProperty.createProperty(this, Integer.class, "bot"),};
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return filterProperties;
    }

    @Override
    public boolean init(Graph graph) {
        Edge[] edges = graph.getEdges().toArray();
        Arrays.sort(edges, new Comparator<Edge>() {

            @Override
            public int compare(Edge e1, Edge e2) {
                Node n1 = e1.getSource();
                Node n2 = e2.getSource();
                
                if(n1.getId() != n2.getId()) {
                    return n1.getId() - n2.getId();
                } else {
                    double weightDiff = e1.getWeight() - e2.getWeight();
                    if(weightDiff > 0) {
                        return 1;
                    } else if(weightDiff < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        
        this.filteredEdges = new HashSet<Edge>();
        
        // add bottom n edges
        int bot = 0;
        int nodeId = -1;
        for(int i=0;i<edges.length;i++) {
            if(nodeId != edges[i].getSource().getId()) {
                nodeId = edges[i].getSource().getId();
                bot = 0;
            }
            if(bot < this.bot) {
                this.filteredEdges.add(edges[i]);
                bot++;
            }
        }
        
        // add top n edges
        int top = 0;
        nodeId = -1;
        for(int i=edges.length-1;i>=0;i--) {
            if(nodeId != edges[i].getSource().getId()) {
                nodeId = edges[i].getSource().getId();
                top = 0;
            }
            if(top < this.top) {
                if(!this.filteredEdges.contains(edges[i])) {
                    this.filteredEdges.add(edges[i]);
                }
                top++;
            }
        }
        return true;
    }

    @Override
    public boolean evaluate(Graph graph, Edge edge) {
        return this.filteredEdges.contains(edge);
    }

    @Override
    public void finish() {
        this.filteredEdges = null;
    }
    
    public Integer getTop() {
        return this.top;
    }
    
    public void setTop(Integer top) {
        this.top = top;
    }
    
    public Integer getBot() {
        return this.bot;
    }
    
    public void setBot(Integer bot) {
        this.bot = bot;
    }
}
