package storage;

import java.util.Vector;

public class OfferBook {

    private static Vector<String[]> saleOffers = new Vector<String[]>();
    private static Vector<String[]> purchaseOffers = new Vector<String[]>();

    public synchronized static void store(String host, String asset, String offer) {

        String[] splitAsset = asset.split("\\.");
        String[] splitOffer = offer.split(">|;|<");

        String[] data = new String[4];
        int offerId;

        if (splitAsset[0].equals("venda")) {
            data[0] = splitAsset[1];
            for (int i = 1; i <= 3; i++)
                data[i] = splitOffer[i];
            offerId = saleOffers.size();
            saleOffers.add(data);

        } else if (splitAsset[0].equals("compra")) {
            data[0] = splitAsset[1];
            for (int i = 1; i <= 3; i++)
                data[i] = splitOffer[i];
            offerId = purchaseOffers.size();
            purchaseOffers.add(data);

        } else
            return;

        matchOffers(host, splitAsset[0], offerId);
    }

    private synchronized static void matchOffers(String host, String type, int offerId) {

        if (type.equals("compra")) {

            String[] offer = purchaseOffers.get(offerId);

            for (int s = 0; s < saleOffers.size(); s++) {

                String[] sale = saleOffers.get(s);
                float sPrice = Float.parseFloat(sale[2]);
                float oPrice = Float.parseFloat(offer[2]);

                if (sale[0].equals(offer[0]) && oPrice >= sPrice) {

                    int sLength = Integer.parseInt(sale[1]);
                    int oLength = Integer.parseInt(offer[1]);

                    if (sLength > oLength) {
                        Transaction.store(host, sale, offer);
                        sale[1] = Integer.toString(sLength - oLength);
                        saleOffers.set(s, sale);
                        purchaseOffers.remove(offerId);
                        return;

                    } else if (oLength > sLength) {
                        Transaction.store(host, sale, offer);
                        offer[1] = Integer.toString(oLength - sLength);
                        saleOffers.remove(s);
                        purchaseOffers.set(offerId, offer);
                        s--;

                    } else {
                        Transaction.store(host, sale, offer);
                        saleOffers.remove(s);
                        purchaseOffers.remove(offerId);
                        return;
                    }
                }
            }
        } else if (type.equals("venda")) {

            String[] offer = saleOffers.get(offerId);

            for (int p = 0; p < purchaseOffers.size(); p++) {

                String[] purchase = purchaseOffers.get(p);
                float pPrice = Float.parseFloat(purchase[2]);
                float oPrice = Float.parseFloat(offer[2]);

                if (purchase[0].equals(offer[0]) && oPrice <= pPrice) {

                    int pLength = Integer.parseInt(purchase[1]);
                    int oLength = Integer.parseInt(offer[1]);

                    if (pLength > oLength) {
                        Transaction.store(host, offer, purchase);
                        purchase[1] = Integer.toString(pLength - oLength);
                        purchaseOffers.set(p, purchase);
                        saleOffers.remove(offerId);
                        return;

                    } else if (oLength > pLength) {
                        Transaction.store(host, offer, purchase);
                        offer[1] = Integer.toString(oLength - pLength);
                        purchaseOffers.remove(p);
                        saleOffers.set(offerId, offer);
                        p--;

                    } else {
                        Transaction.store(host, offer, purchase);
                        purchaseOffers.remove(p);
                        saleOffers.remove(offerId);
                        return;
                    }
                }
            }
        } else
            return;
    }
}