package cs.arizona.edu.topedgeweightfilter;

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
public class TopEdgeWeightFilter implements EdgeFilter {

    private FilterProperty[] filterProperties;
    private Boolean pernode = false;
    private Integer top = -1;
    private Edge[] sortedEdges;
    private HashSet<Edge> filteredEdges;

    public TopEdgeWeightFilter() {
    }

    @Override
    public String getName() {
        return "Top Edge Weight";
    }

    @Override
    public FilterProperty[] getProperties() {
        if (this.filterProperties == null) {
            this.filterProperties = new FilterProperty[0];
            try {
                this.filterProperties = new FilterProperty[]{
                    FilterProperty.createProperty(this, Boolean.class, "pernode"),
                    FilterProperty.createProperty(this, Integer.class, "top"),};
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return filterProperties;
    }

    @Override
    public boolean init(Graph graph) {
        this.sortedEdges = graph.getEdges().toArray();
        sort();
        this.filteredEdges = new HashSet<Edge>();
        prepareFilteredEdges(this.pernode, this.top);
        return true;
    }

    private void sort() {
        if (this.pernode) {
            Arrays.sort(this.sortedEdges, new Comparator<Edge>() {

                @Override
                public int compare(Edge e1, Edge e2) {
                    Node n1 = e1.getSource();
                    Node n2 = e2.getSource();

                    if (n1.getId() != n2.getId()) {
                        return n1.getId() - n2.getId();
                    } else {
                        double weightDiff = e1.getWeight() - e2.getWeight();
                        if (weightDiff > 0) {
                            return 1;
                        } else if (weightDiff < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            });
        } else {
            Arrays.sort(this.sortedEdges, new Comparator<Edge>() {

                @Override
                public int compare(Edge e1, Edge e2) {
                    double weightDiff = e1.getWeight() - e2.getWeight();
                    if (weightDiff > 0) {
                        return 1;
                    } else if (weightDiff < 0) {
                        return -1;
                    } else {
                        Node n1 = e1.getSource();
                        Node n2 = e2.getSource();

                        if (n1.getId() != n2.getId()) {
                            return n1.getId() - n2.getId();
                        } else {
                            return 0;
                        }
                    }
                }
            });
        }
    }

    private void prepareFilteredEdges(boolean pernode, int top) {
        this.filteredEdges.clear();

        if(top >= 0) {
            if (pernode) {
                // add top n edges
                int cur_top = 0;
                boolean first = true;
                int nodeId = -1;
                for (int i = this.sortedEdges.length - 1; i >= 0; i--) {
                    if (first || nodeId != this.sortedEdges[i].getSource().getId()) {
                        nodeId = this.sortedEdges[i].getSource().getId();
                        cur_top = 0;
                        first = false;
                    }
                    if (cur_top < this.top) {
                        this.filteredEdges.add(this.sortedEdges[i]);
                        cur_top++;
                    }
                }
            } else {
                // add top n edges
                int cur_top = 0;
                for (int i = this.sortedEdges.length - 1; i >= 0; i--) {
                    if (cur_top < this.top) {
                        this.filteredEdges.add(this.sortedEdges[i]);
                        cur_top++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean evaluate(Graph graph, Edge edge) {
        if(this.top < 0) {
            return true;
        }
        
        return this.filteredEdges.contains(edge);
    }

    @Override
    public void finish() {
        this.sortedEdges = null;
        this.filteredEdges = null;
    }

    public Boolean getPernode() {
        return this.pernode;
    }

    public void setPernode(boolean perNode) {
        this.pernode = perNode;
    }
    
    public void setPernode(Boolean perNode) {
        this.pernode = perNode;
    }

    public Integer getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }
    
    public void setTop(Integer top) {
        this.top = top;
    }
}
