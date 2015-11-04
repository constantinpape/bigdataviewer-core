package bdv.spimdata.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mpicbg.spim.data.generic.AbstractSpimData;
import mpicbg.spim.data.generic.base.EntityUtils;
import mpicbg.spim.data.generic.sequence.AbstractSequenceDescription;
import mpicbg.spim.data.generic.sequence.AbstractSequenceDescriptionUtils;
import mpicbg.spim.data.generic.sequence.BasicViewSetup;
import mpicbg.spim.data.registration.ViewRegistrations;
import mpicbg.spim.data.registration.ViewRegistrationsUtils;

public class ChangeViewSetupId
{
	/**
	 * For the specified {@code spimData} data set, re-assign setup id's that
	 * are already in use. As a result, setup ids in {@code spimData} are
	 * updated and the set of used ids {@code idsInUse} is updated.
	 * <p>
	 * This method takes care af re-assigning ids in the setups themselves, as
	 * well as in the {@link ViewRegistrations}.
	 *
	 * @param spimData
	 *            dataset whose setups to make unique. Will be updated as a
	 *            result of this method.
	 * @param idsInUse
	 *            Set of setup ids already used. Will be updated as a result of
	 *            this method.
	 * @return a map from old id to new id (the setups in {@code spimData} had
	 *         the old id before calling this method, and have the new id after
	 *         calling this method).
	 */
	public static Map< Integer, Integer > assignNewViewSetupIds(
			final AbstractSpimData< ? > spimData,
			final Set< Integer > idsInUse )
	{
		final AbstractSequenceDescription< ?, ?, ? > seq = spimData.getSequenceDescription();
		final List< ? extends BasicViewSetup > setups = seq.getViewSetupsOrdered();

		// maps old setup id to new setup id
		final Map< Integer, Integer > oldIdToNewIdMap = new HashMap< Integer, Integer >();

		for ( final BasicViewSetup setup : setups )
		{
			final int oldId = setup.getId();
			int newId = oldId;
			while ( idsInUse.contains( newId ) )
				++newId;
			if ( newId != oldId )
				oldIdToNewIdMap.put( oldId, newId );
			idsInUse.add( newId );
		}

		AbstractSequenceDescriptionUtils.changeIds( seq, oldIdToNewIdMap );

		final ViewRegistrations regs = spimData.getViewRegistrations();
		ViewRegistrationsUtils.changeViewsetupIds( regs, oldIdToNewIdMap );

		return oldIdToNewIdMap;
	}

	/**
	 * TODO
	 *
	 * @param spimData
	 * @param oldViewSetupId
	 * @param newViewSetupId
	 */
	public static void changeViewSetupId(
			final AbstractSpimData< ? > spimData,
			final int oldViewSetupId,
			final int newViewSetupId )
	{
		final List< ? extends BasicViewSetup > setups = spimData.getSequenceDescription().getViewSetupsOrdered();
		EntityUtils.changeIds( setups, oldViewSetupId, newViewSetupId );

		final ViewRegistrations viewRegistrations = spimData.getViewRegistrations();
		ViewRegistrationsUtils.changeViewsetupIds( viewRegistrations, oldViewSetupId, newViewSetupId );
	}
}