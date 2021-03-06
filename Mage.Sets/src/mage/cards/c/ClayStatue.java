package mage.cards.c;

import mage.MageInt;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.RegenerateSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;

import java.util.UUID;

/**
 *
 * @author Jgod
 */
public class ClayStatue extends CardImpl {

    public ClayStatue(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ARTIFACT,CardType.CREATURE},"{4}");
        this.subtype.add("Golem");
        this.power = new MageInt(3);
        this.toughness = new MageInt(1);

        // {2}: Regenerate Clay Statue.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new RegenerateSourceEffect(), new ManaCostsImpl("{2}")));
    }

    public ClayStatue(final ClayStatue card) {
        super(card);
    }

    @Override
    public ClayStatue copy() {
        return new ClayStatue(this);
    }
}