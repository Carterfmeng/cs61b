package byow.Core;


public class Room {
    private Position ldPos;
    private Position ruPos;
    private boolean isMarked;
    /** use storeEdges() to preserve the edges once the room is drawn to the world.
     * Otherwise, is null. Indices for edges: bottom: 0, left: 1, top: 2, right: 3 */
    private Edge[] edges;

    /** Edge start from startPos, end at endPos, A room has two edges start from ldPos, two
     * edges start from ruPos.*/
    private class Edge {
        int edgeLen;
        Position startPos;
        Position endPos;
        boolean isHorizontal;
        boolean isLdStartEdge;

        Edge(Position startPos, Position endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
            computeEdgeAttributes();
        }

        void computeEdgeAttributes() {
            if (startPos.getX() == endPos.getX()) {
                isHorizontal = false;
                int posDis = startPos.getY() - endPos.getY();
                edgeLen = Math.abs(posDis) + 1;
                if (posDis < 0) {
                    isLdStartEdge = true;
                } else {
                    isLdStartEdge = false;
                }
            } else {
                isHorizontal = true;
                int posDis = startPos.getX() - endPos.getX();
                edgeLen = Math.abs(posDis) + 1;
                if (posDis < 0) {
                    isLdStartEdge = true;
                } else {
                    isLdStartEdge = false;
                }
            }
        }
    }

    public Room(Position ldPos, Position ruPos) {
        this.ldPos = ldPos;
        this.ruPos = ruPos;
        this.isMarked = false;
        this.edges = null;
    }

    public Edge computeEdge(Position startPos, Position endPos) {
        return new Edge(startPos, endPos);
    }

    /** preserve the edges once the room is created to the world. Otherwise, is null.
     * indices for edges: bottom: 0, left: 1, top: 2, right: 3 */
    public void storeEdges() {
        this.edges = new Edge[4];
        this.edges[0] = computeEdge(ldPos, new Position(ruPos.getX(), ldPos.getY()));
        this.edges[1] = computeEdge(ldPos, new Position(ldPos.getX(), ruPos.getY()));
        this.edges[2] = computeEdge(ruPos, new Position(ldPos.getX(), ruPos.getY()));
        this.edges[3] = computeEdge(ruPos, new Position(ruPos.getX(), ldPos.getY()));
    }

    public Position getLdPos() {
        return this.ldPos;
    }

    public Position getRuPos() {
        return this.ruPos;
    }

    public void setMarked() {
        this.isMarked = true;
    }

    public boolean getIsMarked() {
        return this.isMarked;
    }

}
