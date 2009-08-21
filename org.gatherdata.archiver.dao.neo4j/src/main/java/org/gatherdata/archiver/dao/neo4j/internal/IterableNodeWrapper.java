package org.gatherdata.archiver.dao.neo4j.internal;

import java.util.Iterator;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.neo4j.api.core.Node;

public class IterableNodeWrapper<T> implements Iterable<T> {
    
    Iterable<Node> nodes;
    NodeAdapter<T> nodeAdapter;
    
    public IterableNodeWrapper(Iterable<Node> nodes, NodeAdapter<T> nodeAdapter) {
        this.nodes = nodes;
        this.nodeAdapter = nodeAdapter;
    }

    public Iterator<T> iterator() {
        return new NodeIteratorWrapper(nodes.iterator());
    }

    class NodeIteratorWrapper implements Iterator<T> {
        
        Iterator<Node> wrappedIterator;

        public NodeIteratorWrapper(Iterator<Node> iteratorToWrap) {
            wrappedIterator = iteratorToWrap;
        }

        public boolean hasNext() {
            return wrappedIterator.hasNext();
        }

        public T next() {
            Node nextNode = wrappedIterator.next();
            return nodeAdapter.adaptFromNode(nextNode);
        }

        public void remove() {
            wrappedIterator.remove();
        }
        
    }
}
