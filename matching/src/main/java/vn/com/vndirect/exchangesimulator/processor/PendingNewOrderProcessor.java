package vn.com.vndirect.exchangesimulator.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vn.com.vndirect.exchangesimulator.datastorage.order.Storage;
import vn.com.vndirect.exchangesimulator.model.ExecType;
import vn.com.vndirect.exchangesimulator.model.ExecutionReport;
import vn.com.vndirect.exchangesimulator.model.HnxMessage;
import vn.com.vndirect.exchangesimulator.model.NewOrderSingle;
import vn.com.vndirect.exchangesimulator.model.OrdStatus;
import vn.com.vndirect.exchangesimulator.validator.AccountValidator;
import vn.com.vndirect.exchangesimulator.validator.NewOrderSingleValidator;
import vn.com.vndirect.exchangesimulator.validator.OrderTypeValidator;
import vn.com.vndirect.exchangesimulator.validator.PriceValidator;
import vn.com.vndirect.exchangesimulator.validator.QuantityValidator;
import vn.com.vndirect.exchangesimulator.validator.SessionValidator;
import vn.com.vndirect.exchangesimulator.validator.SideValidator;
import vn.com.vndirect.exchangesimulator.validator.SymbolValidator;
import vn.com.vndirect.exchangesimulator.validator.exception.ValidateException;

public class PendingNewOrderProcessor implements Processor {
	
	private static final Logger LOGGER = Logger.getLogger(PendingNewOrderProcessor.class);
	
	private Storage<NewOrderSingle> orderStorage;
	private NewOrderSingleValidator validator; 
	
	public PendingNewOrderProcessor(Storage<NewOrderSingle> orderStorage, NewOrderSingleValidator validator) {
		this.orderStorage = orderStorage;
		this.validator = validator;
		this.validator = new NewOrderSingleValidator() {
			
			@Override
			public void validate(NewOrderSingle order) throws ValidateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public SymbolValidator getSymbolValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public SideValidator getSideValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public SessionValidator getSessionValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public QuantityValidator getQuantityValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PriceValidator getPriceValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public OrderTypeValidator getOrderTypeValidator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AccountValidator getAccountValidator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public List<ExecutionReport> process(HnxMessage message) {
		List<ExecutionReport> executionReports = new ArrayList<ExecutionReport>();
		NewOrderSingle newOrderSingle = (NewOrderSingle) message; 
		ExecutionReport executionReport = buildConfirmOrder(newOrderSingle);
		try {
			validator.validate(newOrderSingle);
			storeOrder(newOrderSingle);
		} catch (ValidateException e) {
			LOGGER.error("Order validation fail: " + newOrderSingle, e);
			executionReport.setMsgType("3");
			executionReport.setRefSeqNum(message.getMsgSeqNum());
			executionReport.setText(e.getMessage());
			executionReport.setOrdRejReason("4");
			executionReport.setSessionRejectReason(e.getCode());
			executionReport.setOrdStatus(OrdStatus.REJECT);
			executionReport.setExecType(ExecType.REJECT);
			executionReport.setUnderlyingLastQty(newOrderSingle.getOrderQty());
			executionReport.setRefMsgType(message.getMsgType());
		}
		executionReports.add(executionReport);
		return executionReports;
	}

	protected ExecutionReport buildConfirmOrder(NewOrderSingle message) {
		return PendingNewReportGenerator.report(message);
	}
	
	private void storeOrder(final NewOrderSingle newOrderSingle) {
		orderStorage.add(newOrderSingle);
	}
	
}
