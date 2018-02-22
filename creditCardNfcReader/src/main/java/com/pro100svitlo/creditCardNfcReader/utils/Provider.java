package com.pro100svitlo.creditCardNfcReader.utils;

import android.nfc.tech.IsoDep;

import com.pro100svitlo.creditCardNfcReader.enums.SwEnum;
import com.pro100svitlo.creditCardNfcReader.exception.CommunicationException;
import com.pro100svitlo.creditCardNfcReader.parser.IProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Provider implements IProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(Provider.class);

    private IsoDep mTagCom;

    public void setmTagCom(final IsoDep mTagCom) {
        this.mTagCom = mTagCom;
    }

    @Override
    public byte[] transceive(byte[] pCommand) throws CommunicationException {
        byte[] response = null;
        try {
            // send command to emv card
            response = mTagCom.transceive(pCommand);
        } catch (IOException e) {
            throw new CommunicationException(e.getMessage());
        }

        LOGGER.debug("resp: " + BytesUtils.bytesToString(response));
        try {
            LOGGER.debug("resp: " + TlvUtil.prettyPrintAPDUResponse(response));
            SwEnum val = SwEnum.getSW(response);
            if (val != null) {
                LOGGER.debug("resp: " + val.getDetail());
            }
        } catch (Exception e) {
        }

        return response;
    }
}
