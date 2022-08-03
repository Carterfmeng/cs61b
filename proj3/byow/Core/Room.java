package byow.Core;


import java.util.Random;

import static byow.Core.DrawUtils.*;

public class Room {
    private Position ldPos;
    private Position ruPos;
    private boolean isMarked;
    /** use storeEdges() to preserve the edges once the room is drawn to the world.
     * Otherwise, is null. Indices for edges: bottom: 0, left: 1, top: 2, right: 3 */
    private Edge[] edges;

    /** Edge start from startPos, end at endPos, A room has two edges start from ldPos, two
     * edges start from ruPos.*/
    class Edge {
        int edgeLen;
        Position startPos;
        Position endPos;
        boolean isHorizontal;
        boolean isLdStartEdge;
        int edgeIndex;

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
                    edgeIndex = 1;
                    isLdStartEdge = true;
                } else {
                    edgeIndex = 3;
                    isLdStartEdge = false;
                }
            } else {
                isHorizontal = true;
                int posDis = startPos.getX() - endPos.getX();
                edgeLen = Math.abs(posDis) + 1;
                if (posDis < 0) {
                    edgeIndex = 0;
                    isLdStartEdge = true;
                } else {
                    edgeIndex = 2;
                    isLdStartEdge = false;
                }
            }
        }

        Position shiftPosOnTheEdge(Position toShiftedPos) {
            if (edgeIndex == 0) {
                return toShiftedPos.shiftPosition(0, -1);
            } else if (edgeIndex == 1) {
                return toShiftedPos.shiftPosition(-1, 0);
            } else if (edgeIndex == 2) {
                return toShiftedPos.shiftPosition(0, 1);
            } else if (edgeIndex == 3) {
                return toShiftedPos.shiftPosition(1, 0);
            }
            return null;
        }

        Room generateARoomOnEdge(Random random, Position startPos, int xLen, int yLen) {
            Position ldPos;
            Position ruPos;
            if (this.edgeIndex == 0) {
                Position ruPosRangeS = startPos.shiftPosition(1, 0);
                Position ruPosRangeE = startPos.shiftPosition(xLen - 2, 0);
                ruPos = getRandomPos(random, new Room(ruPosRangeS, ruPosRangeE));
                ldPos = ruPos.shiftPosition(-xLen + 1, -yLen + 1);
                return new Room(ldPos,ruPos);
            } else if (this.edgeIndex == 1) {
                Position ruPosRangeS = startPos.shiftPosition(0, 1);
                Position ruPosRangeE = startPos.shiftPosition(0, yLen - 2);
                ruPos = getRandomPos(random, new Room(ruPosRangeS, ruPosRangeE));
                ldPos = ruPos.shiftPosition(-xLen + 1, -yLen + 1);
                return new Room(ldPos,ruPos);
            } else if (this.edgeIndex == 2) {
                Position ldPosRangeS = startPos.shiftPosition(-xLen + 2, 0);
                Position ldPosRangeE = startPos.shiftPosition(-1, 0);
                ldPos = getRandomPos(random, new Room(ldPosRangeS, ldPosRangeE));
                ruPos = ldPos.shiftPosition(xLen - 1, yLen - 1);
                return new Room(ldPos,ruPos);
            } else if (this.edgeIndex == 3) {
                Position ldPosRangeS = startPos.shiftPosition(0, -yLen + 2);
                Position ldPosRangeE = startPos.shiftPosition(0, -1);
                ldPos = getRandomPos(random, new Room(ldPosRangeS, ldPosRangeE));
                ruPos = ldPos.shiftPosition(xLen - 1, yLen - 1);
                return new Room(ldPos,ruPos);
            }
            return null;
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

    public int getXLen() {
        return this.ruPos.getX() - this.ldPos.getX() + 1;
    }

    public int getYLen() {
        return this.ruPos.getY() - this.ldPos.getY() + 1;
    }

    public Position getLuPos() {
        int LuPosX = this.ldPos.getX();
        int LuPosY = this.ldPos.getY() + getYLen() - 1;
        return new Position(LuPosX, LuPosY);
    }

    public Position getRdPos() {
        int RdPosX = this.ruPos.getX();
        int RdPosY = this.ruPos.getY() - getYLen() + 1;
        return new Position(RdPosX, RdPosY);
    }

    public Edge[] getEdges() {
        return this.edges;
    }

    public void setMarked() {
        this.isMarked = true;
    }

    public boolean getIsMarked() {
        return this.isMarked;
    }

}
